package hello.entity;

import java.util.List;

public class BlogResult extends Result {
    private Integer total;
    private Integer page;
    private Integer totalPage;

    public BlogResult(String status, String msg, List<Blog> data, Integer total, Integer page, Integer totalPage) {
        super(status, msg, data);
        this.total = total;
        this.page = page;
        this.totalPage = totalPage;
    }

    public static BlogResult newBlogs(List<Blog> blogs, Integer count, Integer pageCount, Integer pageSize) {
        return new BlogResult("ok", "获取成功", blogs, count, pageCount, pageSize);
    }

    public static BlogResult failure(String msg) {
        return new BlogResult("fail", msg,null,0,0,0);
    }

    public Integer getTotal() {
        return total;
    }

    public Integer getPage() {
        return page;
    }

    public Integer getTotalPage() {
        return totalPage;
    }
}
