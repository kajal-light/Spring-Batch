package com.springbootbatch.demo.config;

import com.springbootbatch.demo.entity.CutomerEntity;
import org.springframework.batch.item.ItemProcessor;

public class SpringProcessor implements ItemProcessor<CutomerEntity,CutomerEntity> {


    @Override
    public CutomerEntity process(CutomerEntity cutomerEntity) throws Exception {
        return cutomerEntity;
    }
}
