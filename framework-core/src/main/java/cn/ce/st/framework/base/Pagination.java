package cn.ce.st.framework.base;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
* @Author houxi
* @Description
* @Date 9:52 2019/4/12
* @Param 
* return 
**/
public class Pagination<T> implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final int DEFAULT_CURRENTPAGE = 1;

    /** 每页默认10条数据*/
    private int pageSize;
    /** 当前页*/
    private int currentPage;
    /**  总页数*/
    private int totalPages;
    /** 总数据数*/
    private int totalCount;
    /** 记录集合*/
    private List<T> list;
    /** 分页默认的分页大小属性名，其值通过前端控制器进行传递*/
    public static final String DEFAULT_PAGE_SIZE_ATTR = "defaultPageSize";


    private Map<String, Object> params = new HashMap<>();

    public Pagination(int totalCount, int pageSize) {
        this.init(totalCount, pageSize);
    }

    public Pagination() {
        this.pageSize = DEFAULT_PAGE_SIZE;
        this.currentPage = DEFAULT_CURRENTPAGE;
    }

    /**
     * @return
     * @方法功能说明：获取参数列表（不包括分页参数)
     */
    public Map<String, Object> getParams() {
        return params;
    }

    /**
     * @param key
     * @param value
     * @方法功能说明：为分页对象传递参数
     */
    public void putParamValue(String key, Object value) {
        params.put(key, value);
    }

    /**
     * @param key
     * @return
     * @方法功能说明：获取分页对象的参数
     */
    public Object getParamValue(String key) {
        return params.get(key);
    }

    /**
     * @方法功能说明：清空传递的参数
     */
    public void clearParam() {
        params.clear();
    }


    /**
     * 初始化分页参数:需要先设置totalRows
     */
    public void init(int totalCount, int pageSize) {
        this.pageSize = pageSize;
        this.totalCount = totalCount;
        if ((totalCount % pageSize) == 0) {
            totalPages = totalCount / pageSize;
        } else {
            totalPages = totalCount / pageSize + 1;
        }
        if (this.currentPage > totalPages && totalPages > 0) {
            this.currentPage = totalPages;
        } else if (this.currentPage < 1) {
            this.currentPage = 1;
        }
    }

    public void init(int totalCount, int pageSize, int currentPage) {
        this.currentPage = currentPage;
        this.init(totalCount, pageSize);
    }

    public void init(List<T> list) {
        this.list = list;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public void setTotalPages(int totalPages) {
        this.totalPages = totalPages;
    }

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "Pagination{" +
                "pageSize=" + pageSize +
                ", currentPage=" + currentPage +
                ", totalPages=" + totalPages +
                ", totalCount=" + totalCount +
                ", list=" + list +
                ", params=" + params +
                '}';
    }
}
