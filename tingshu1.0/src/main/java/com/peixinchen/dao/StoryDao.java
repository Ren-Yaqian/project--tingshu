package com.peixinchen.dao;

import com.peixinchen.model.Story;
import com.peixinchen.util.DBUtil;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StoryDao {
    public Story selectOneUsingSid(int sid) throws SQLException {
        try (Connection c = DBUtil.getConnection()) {
            String sql = "SELECT name, created_at, count FROM story WHERE sid = ?";
            try (PreparedStatement s = c.prepareStatement(sql)) {
                s.setInt(1, sid);

                try (ResultSet rs = s.executeQuery()) {
                    if (!rs.next()) {
                        return null;
                    }

                    Story story = new Story();
                    story.sid = sid;
                    story.name = rs.getString("name");
                    story.createdAt = rs.getDate("created_at");
                    story.count = rs.getInt("count");
                    return story;
                }
            }
        }
    }

    public List<Story> selectListUsingAid(int aid) throws SQLException {
        List<Story> storyList = new ArrayList<>();
        try (Connection c = DBUtil.getConnection()) {
            String sql = "SELECT sid, name, created_at, count FROM story WHERE aid = ? ORDER BY sid ASC";
            try (PreparedStatement s = c.prepareStatement(sql)) {
                s.setInt(1, aid);
                try (ResultSet rs = s.executeQuery()) {
                    while (rs.next()) {
                        Story story = new Story();
                        story.sid = rs.getInt("sid");
                        story.name = rs.getString("name");
                        story.createdAt = rs.getDate("created_at");
                        story.count = rs.getInt("count");

                        storyList.add(story);
                    }
                }
            }
        }
        return storyList;
    }

    public InputStream selectOneAudioColumnUsingSid(int sid) throws SQLException {
        try (Connection c = DBUtil.getConnection()) {
            String sql = "SELECT audio FROM story WHERE sid = ?";
            try (PreparedStatement s = c.prepareStatement(sql)) {
                s.setInt(1, sid);
                try (ResultSet rs = s.executeQuery()) {
                    if (!rs.next()) {
                        return null;
                    }

                    return rs.getBinaryStream("audio");
                }
            }
        }
    }

    public void insert(int aid, String name, InputStream audio) throws SQLException {
        try (Connection c = DBUtil.getConnection()) {
            String sql = "INSERT INTO story (aid, name, audio) VALUES (?, ?, ?)";
            try (PreparedStatement s = c.prepareStatement(sql)) {
                s.setInt(1, aid);
                s.setString(2, name);
                s.setBlob(3, audio);

                s.executeUpdate();
            }
        }
    }
}
