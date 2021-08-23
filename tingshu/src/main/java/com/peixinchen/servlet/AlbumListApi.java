package com.peixinchen.servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.peixinchen.model.Album;
import com.peixinchen.util.DBUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/api/album-list.json")
public class AlbumListApi extends HttpServlet {
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf-8");
        String keyword = req.getParameter("keyword");
        if (keyword != null && keyword.trim().isEmpty()) {
            keyword = null;
        }

        resp.setCharacterEncoding("utf-8");
        resp.setContentType("application/json");
        PrintWriter writer = resp.getWriter();

        String sql;
        if (keyword != null) {
            sql = "SELECT aid, name, cover, count FROM album WHERE name LIKE ? ORDER BY aid DESC LIMIT 20";
        } else {
            sql = "SELECT aid, name, cover, count FROM album ORDER BY aid DESC LIMIT 20";
        }

        List<Album> albumList = new ArrayList<>();
        try (Connection c = DBUtil.getConnection()) {
            try (PreparedStatement s = c.prepareStatement(sql)) {
                if (keyword != null) {
                    // SQL: WHERE name LIKE %hello%: 只要名字中有 hello 就匹配
                    s.setString(1, "%" + keyword + "%");
                }

                try (ResultSet rs = s.executeQuery()) {
                    while (rs.next()) {
                        Album album = new Album();

                        album.aid = rs.getInt("aid");
                        album.name = rs.getString("name");
                        album.cover = rs.getString("cover");
                        album.count = rs.getInt("count");

                        albumList.add(album);
                    }
                }
            }

            Object result = new Object() {
                public final boolean success = true;
                public final Object data = albumList;
            };
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
            writer.println(json);
        } catch (SQLException exc) {
            exc.printStackTrace();

            Object result = new Object() {
                public final boolean success = false;
                public final String reason = exc.getMessage();
            };
            String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
            writer.println(json);
        }
    }
}
