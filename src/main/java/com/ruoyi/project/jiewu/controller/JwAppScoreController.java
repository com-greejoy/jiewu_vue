package com.ruoyi.project.jiewu.controller;

import com.ruoyi.framework.web.controller.BaseController;
import com.ruoyi.framework.web.domain.AjaxResult;
import com.ruoyi.project.jiewu.service.JwGameItemService;
import com.ruoyi.project.jiewu.service.JwMatchService;
import com.ruoyi.project.jiewu.service.JwSignRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/app/score/api")
public class JwAppScoreController extends BaseController {

    @Autowired
    private JwMatchService jwMatchService;

    @Autowired
    private JwGameItemService jwGameItemService;

    @Autowired
    private JwSignRecordService jwSignRecordService;

    @PostMapping("/getJudgeList")
    @ResponseBody
    public AjaxResult getJudgeList() {

        return AjaxResult.success(1);
    }

    @PostMapping("/judgeLogin")
    @ResponseBody
    public AjaxResult judgeLogin(Long judgeId, String judgeName) {

        return AjaxResult.success(1);
    }
}