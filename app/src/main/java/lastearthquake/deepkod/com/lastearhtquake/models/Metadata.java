
package lastearthquake.deepkod.com.lastearhtquake.models;


import com.google.gson.annotations.Expose;

public class Metadata {

    @Expose
    private Integer generated;
    @Expose
    private String url;
    @Expose
    private String title;
    @Expose
    private Integer status;
    @Expose
    private String api;
    @Expose
    private Integer limit;
    @Expose
    private Integer offset;
    @Expose
    private Integer count;

    public Integer getGenerated() {
        return generated;
    }

    public void setGenerated(Integer generated) {
        this.generated = generated;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

}
