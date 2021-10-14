package com.seeyon.apps.evection.manager.impl;

import com.seeyon.apps.evection.dao.EvectionDao;
import com.seeyon.apps.evection.manager.EvectionManager;
import org.springframework.beans.factory.annotation.Autowired;

public class EvectionManagerImpl implements EvectionManager {
    @Autowired
    private EvectionDao evectionDao;
    @Override
    public String getIdCardByMemberId(String memberId) {
        return evectionDao.getIdCardByMemberId(memberId);
    }
}
