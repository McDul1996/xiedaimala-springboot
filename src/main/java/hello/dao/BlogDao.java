package hello.dao;

import hello.entity.Blog;
import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class BlogDao {
    private final SqlSession sqlSession;

    @Inject
    public BlogDao(SqlSession sqlSession) {
        this.sqlSession = sqlSession;
    }

    public List<Blog> getBlogs(Integer page, Integer pageSize, Integer userId) {
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", userId);
        map.put("offset", (page - 1) * pageSize);
        map.put("limit", pageSize);
        return sqlSession.selectList("selectBlog", map);
    }

    public int count(Integer userId) {
        return sqlSession.selectOne("countBlog",userId);
    }
}