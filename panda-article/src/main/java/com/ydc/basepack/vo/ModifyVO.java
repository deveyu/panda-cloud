package com.ydc.basepack.vo;

/**
 * @author ydc
 * @description 单个检查结果
 */
public class ModifyVO {
    private String inspectType;
    private String content;
    private String inspectCategory;
    private String lookup;
    private String offset;
    private String paragraphIndex;
    private String detail;


    public String getInspectType() {
        return inspectType;
    }

    public void setInspectType(String inspectType) {
        this.inspectType = inspectType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getInspectCategory() {
        return inspectCategory;
    }

    public void setInspectCategory(String inspectCategory) {
        this.inspectCategory = inspectCategory;
    }

    public String getLookup() {
        return lookup;
    }

    public void setLookup(String lookup) {
        this.lookup = lookup;
    }

    public String getOffset() {
        return offset;
    }

    public void setOffset(String offset) {
        this.offset = offset;
    }

    public String getParagraphIndex() {
        return paragraphIndex;
    }

    public void setParagraphIndex(String paragraphIndex) {
        this.paragraphIndex = paragraphIndex;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }


    @Override
    public String toString() {
        return "ModifyVO{" +
                "inspectType='" + inspectType + '\'' +
                ", content='" + content + '\'' +
                ", inspectCategory='" + inspectCategory + '\'' +
                ", lookup='" + lookup + '\'' +
                ", offset='" + offset + '\'' +
                ", paragraphIndex='" + paragraphIndex + '\'' +
                ", detail='" + detail + '\'' +
                '}';
    }
}
