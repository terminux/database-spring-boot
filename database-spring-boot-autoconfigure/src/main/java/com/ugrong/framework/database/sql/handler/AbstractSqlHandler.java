package com.ugrong.framework.database.sql.handler;


import com.ugrong.framework.database.config.properties.DatabaseProperties;
import com.ugrong.framework.database.enums.EnumSqlType;
import com.ugrong.framework.database.vo.InsertSqlInfo;
import com.ugrong.framework.database.vo.SqlInfo;
import com.ugrong.framework.database.vo.UpdateSqlInfo;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Abstract sql handler.
 */
public abstract class AbstractSqlHandler implements SqlHandler {

    protected DatabaseProperties databaseProperties;

    @Override
    public void handle(SqlInfo info) {
        if (info == null || StringUtils.isBlank(info.getSql()) || info.getSqlType() == null) {
            return;
        }
        List<String> excludeTables = databaseProperties.getExcludeSqlTables();
        String tableName = info.getTableName();
        if (CollectionUtils.isNotEmpty(excludeTables) && StringUtils.isNotBlank(tableName) && this.containsTable(tableName, excludeTables)) {
            return;
        }
        this.handleByType(info);
    }

    private boolean containsTable(String tableName, List<String> excludeTables) {
        for (String excludeTable : excludeTables) {
            Pattern pattern = Pattern.compile(excludeTable);
            Matcher matcher = pattern.matcher(tableName);
            if (matcher.find()) {
                return true;
            }
        }
        return false;
    }

    protected void handleByType(SqlInfo info) {
        EnumSqlType sqlType = info.getSqlType();
        switch (sqlType) {
            case INSERT:
                if (info instanceof InsertSqlInfo) {
                    this.handleInsert((InsertSqlInfo) info);
                }
                break;
            case UPDATE:
                if (info instanceof UpdateSqlInfo) {
                    UpdateSqlInfo update = (UpdateSqlInfo) info;
                    if (update.isLogicDelete()) {
                        //逻辑删除
                        this.handleLogicDelete(update);
                        return;
                    }
                    this.handleUpdate(update);
                }
                break;
            case DELETE:
                this.handleDelete(info);
                break;
            case SELECT:
                this.handleSelect(info);
                break;
            case UNKNOWN:
                this.handleUnknown(info);
                break;
            default:
        }
    }

    /**
     * Handle insert.
     *
     * @param info the info
     */
    protected void handleInsert(InsertSqlInfo info) {

    }

    /**
     * Handle update.
     *
     * @param info the info
     */
    protected void handleUpdate(UpdateSqlInfo info) {

    }

    /**
     * 逻辑删除.
     *
     * @param info the info
     */
    protected void handleLogicDelete(UpdateSqlInfo info) {

    }

    /**
     * 物理删除.
     *
     * @param info the info
     */
    protected void handleDelete(SqlInfo info) {

    }

    /**
     * Handle select.
     *
     * @param info the info
     */
    protected void handleSelect(SqlInfo info) {

    }

    /**
     * Handle unknown.
     *
     * @param info the info
     */
    protected void handleUnknown(SqlInfo info) {

    }
}
