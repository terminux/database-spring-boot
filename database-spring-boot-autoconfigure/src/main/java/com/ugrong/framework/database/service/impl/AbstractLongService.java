package com.ugrong.framework.database.service.impl;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.ugrong.framework.database.model.LongModel;
import com.ugrong.framework.database.service.ILongService;


public abstract class AbstractLongService<Mapper extends BaseMapper<Entity>, Entity extends LongModel>
        extends AbstractBaseService<Mapper, Entity, Long> implements ILongService<Entity> {


}
