package com.peixinchen.service;

import com.peixinchen.dao.StoryDao;
import com.peixinchen.model.Story;

import java.io.InputStream;
import java.sql.SQLException;

public class StoryService {
    private final StoryDao storyDao = new StoryDao();

    public Story detail(int sid) throws SQLException {
        return storyDao.selectOneUsingSid(sid);
    }

    public InputStream getAudio(int sid) throws SQLException {
        return storyDao.selectOneAudioColumnUsingSid(sid);
    }

    public void save(int aid, String name, InputStream is) throws SQLException {
        storyDao.insert(aid, name, is);
    }
}
