package com.ruoyi.project.jiewu.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.framework.aspectj.lang.annotation.Excel;
import com.ruoyi.framework.web.domain.BaseEntity;

import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * 选手对象 jw_sport
 * 
 * @author ruoyi
 * @date 2024-05-29
 */
public class JwSport extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long id;

    @NotEmpty(message = "姓名不能为空")
    @Excel(name = "姓名")
    private String playerName;

    @NotEmpty(message = "身份证不能为空")
    @Excel(name = "身份证")
    private String idCard;

//    @Excel(name = "性别")
    private String sex;

//    @Excel(name = "年龄")
    private Long age;

//    @Excel(name = "创建者")
    private Long createUserId;

    private Boolean select;
    private Long matchId;
    private Long teamId;
    private String backNumber;
    private String teamName;

    private List<JwGameItem> jwGameItemList;


    public String getTeamName() {
        return teamName;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public String getBackNumber() {
        return backNumber;
    }

    public void setBackNumber(String backNumber) {
        this.backNumber = backNumber;
    }

    public Long getMatchId() {
        return matchId;
    }

    public void setMatchId(Long matchId) {
        this.matchId = matchId;
    }

    public Long getTeamId() {
        return teamId;
    }

    public void setTeamId(Long teamId) {
        this.teamId = teamId;
    }

    public List<JwGameItem> getJwGameItemList() {
        return jwGameItemList;
    }

    public void setJwGameItemList(List<JwGameItem> jwGameItemList) {
        this.jwGameItemList = jwGameItemList;
    }

    public Boolean getSelect() {
        return select;
    }

    public void setSelect(Boolean select) {
        this.select = select;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setPlayerName(String playerName) 
    {
        this.playerName = playerName;
    }

    public String getPlayerName() 
    {
        return playerName;
    }
    public void setIdCard(String idCard) 
    {
        this.idCard = idCard;
    }

    public String getIdCard() 
    {
        return idCard;
    }
    public void setSex(String sex) 
    {
        this.sex = sex;
    }

    public String getSex() 
    {
        return sex;
    }
    public void setAge(Long age)
    {
        this.age = age;
    }

    public Long getAge()
    {
        return age;
    }
    public void setCreateUserId(Long createUserId) 
    {
        this.createUserId = createUserId;
    }

    public Long getCreateUserId() 
    {
        return createUserId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("playerName", getPlayerName())
            .append("idCard", getIdCard())
            .append("sex", getSex())
            .append("age", getAge())
            .append("createUserId", getCreateUserId())
            .toString();
    }
}
