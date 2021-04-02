package com.ugrong.framework.database.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ugrong.framework.database.model.StringModel;
import com.ugrong.framework.database.service.IStringService;

public abstract class AbstractStringService<Mapper extends BaseMapper<Entity>, Entity extends StringModel>
        extends AbstractBaseService<Mapper, Entity, String> implements IStringService<Entity> {

}
