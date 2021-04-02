package com.ugrong.framework.database.sql.listener;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLExpr;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.ast.expr.SQLIdentifierExpr;
import com.alibaba.druid.sql.ast.expr.SQLValuableExpr;
import com.alibaba.druid.sql.ast.statement.SQLExprTableSource;
import com.alibaba.druid.sql.ast.statement.SQLInsertStatement.ValuesClause;
import com.alibaba.druid.sql.ast.statement.SQLTableSource;
import com.alibaba.druid.sql.ast.statement.SQLUpdateSetItem;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlInsertStatement;
import com.alibaba.druid.sql.dialect.mysql.ast.statement.MySqlUpdateStatement;
import com.alibaba.druid.util.JdbcConstants;
import com.ugrong.framework.database.enums.EnumSqlType;
import com.ugrong.framework.database.sql.handler.SqlHandler;
import com.ugrong.framework.database.sql.handler.SqlHandlerContainer;
import com.ugrong.framework.database.utils.SqlUtil;
import com.ugrong.framework.database.vo.InsertSqlInfo;
import com.ugrong.framework.database.vo.SqlInfo;
import com.ugrong.framework.database.vo.UpdateSqlInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class SqlExecuteListenerImpl implements SqlExecuteListener {

    private static final Logger log = LoggerFactory.getLogger(SqlExecuteListenerImpl.class);

    private final SqlHandlerContainer container;

    public SqlExecuteListenerImpl(SqlHandlerContainer container) {
        this.container = container;
    }

    @Override
    @Async
    public void dispatch(String sql, EnumSqlType sqlType) {
        try {
            if (StringUtils.isBlank(sql)) {
                return;
            }
            sqlType = sqlType == null ? SqlUtil.getSqlType(sql) : sqlType;
            List<SqlHandler> handlers = container.get(sqlType);
            if (CollectionUtils.isNotEmpty(handlers)) {
                SqlInfo info = this.parseSql(sql, sqlType);
                log.debug("Start handle sql.SqlInfo=[{}]", info);
                handlers.forEach(handler -> {
                    try {
                        handler.handle(info);
                    } catch (Exception e) {
                        //捕获一下异常 防止因为一个handler处理失败导致中断处理
                        log.error("Failed to handle sql.sqlInfo=[{}],handler=[{}]", info, handler, e);
                    }
                });
            }
        } catch (Exception e) {
            log.error("Failed to dispatch sql.sql=[{}],sqlType=[{}]", sql, sqlType, e);
        }
    }

    private SqlInfo parseSql(String sql, EnumSqlType sqlType) {
        SqlInfo info = this.initSqlInfo(sql, sqlType);
        SQLStatement statement = this.parseStatement(sql);
        info.setStatement(statement);
        this.parseInfo(info);
        return info;
    }

    private SqlInfo initSqlInfo(String sql, EnumSqlType sqlType) {
        SqlInfo info;
        //目前只解析新增和修改语句
        if (sqlType == EnumSqlType.INSERT) {
            //新增
            info = new InsertSqlInfo();
        } else if (sqlType == EnumSqlType.UPDATE) {
            //修改
            info = new UpdateSqlInfo();
        } else {
            //默认
            info = new SqlInfo();
        }
        info.setSql(sql);
        info.setSqlType(sqlType);
        return info;
    }

    private void parseInfo(SqlInfo info) {
        SQLStatement statement = info.getStatement();
        if (statement != null) {
            EnumSqlType sqlType = info.getSqlType();
            if (sqlType == EnumSqlType.INSERT && statement instanceof MySqlInsertStatement) {
                //新增
                this.parseInsertSql((InsertSqlInfo) info, (MySqlInsertStatement) statement);
            } else if (sqlType == EnumSqlType.UPDATE && statement instanceof MySqlUpdateStatement) {
                //修改
                this.parseUpdateSql((UpdateSqlInfo) info, (MySqlUpdateStatement) statement);
            }
        }
    }

    private void parseInsertSql(InsertSqlInfo info, MySqlInsertStatement insert) {
        info.setTableName(this.getTableName(insert.getTableSource()));
        List<SQLExpr> columns = insert.getColumns();
        List<ValuesClause> valuesList = insert.getValuesList();

        if (CollectionUtils.isNotEmpty(columns) && CollectionUtils.isNotEmpty(valuesList)) {
            List<Map<String, String>> values = valuesList.stream()
                    .filter(clause -> clause != null && CollectionUtils.isNotEmpty(clause.getValues()) && clause.getValues().size() == columns.size())
                    .map(clause -> this.buildInsertInfo(clause, columns))
                    .collect(toList());
            info.setValues(values);
        }
    }

    private Map<String, String> buildInsertInfo(ValuesClause clause, List<SQLExpr> columns) {
        List<SQLExpr> clauseValues = clause.getValues();
        Map<String, String> recordInfo = new HashMap<>();
        for (int i = 0; i < columns.size(); i++) {
            SQLIdentifierExpr column = (SQLIdentifierExpr) columns.get(i);
            SQLValuableExpr valueExpr = (SQLValuableExpr) clauseValues.get(i);
            Object value = valueExpr.getValue();
            recordInfo.put(StringUtils.trim(column.getName()), value == null ? null : StringUtils.trim(value.toString()));
        }
        return recordInfo;
    }

    private void parseUpdateSql(UpdateSqlInfo info, MySqlUpdateStatement update) {
        info.setTableName(this.getTableName(update.getTableSource()));
        info.setWhereSql(this.processSql(update.getWhere()));

        List<SQLUpdateSetItem> items = update.getItems();
        if (CollectionUtils.isNotEmpty(items)) {
            Map<String, String> params = items.stream().collect(toMap(item -> this.processSql(item.getColumn()), item -> this.processSql(item.getValue())));
            info.setParams(params);
        }
    }

    private String processSql(SQLExpr expr) {
        if (expr != null) {
            return StringUtils.trim(expr.toString().replaceAll("[\\s]+", StringUtils.SPACE));
        }
        return null;
    }

    private String getTableName(SQLTableSource source) {
        if (source instanceof SQLExprTableSource) {
            SQLExprTableSource exprSource = (SQLExprTableSource) source;
            return exprSource.getName().getSimpleName();
        }
        return null;
    }

    private SQLStatement parseStatement(String sql) {
        List<SQLStatement> statements = null;
        try {
            statements = SQLUtils.parseStatements(sql, JdbcConstants.MYSQL);
        } catch (Exception e) {
            log.error("Failed to parse sql.[{}]", sql);
        }
        if (CollectionUtils.isNotEmpty(statements)) {
            //取第一条 暂不支持复杂SQL
            return statements.get(0);
        }
        return null;
    }
}
