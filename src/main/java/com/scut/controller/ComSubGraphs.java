package com.scut.controller;/*
 * Created by lizeyu on 2020/3/29 15:45
 */

import com.scut.bean.InfoDTO;
import com.scut.comsubgraph.ComSubGraphGetter;
import com.yejh.bean.ComSubGraph;
import com.yejh.titleInit.titleInitializer;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @description: TODO
 **/

@Controller
@RequestMapping(value = "/comSubGraph")
public class ComSubGraphs {

    @RequestMapping(value = "/toQuerySubGraph")
    public String toQuerySubGraph() {
        return "com_sub_graph";
    }

    @RequestMapping(value = "/json", method = RequestMethod.POST)
    @ResponseBody
    public InfoDTO getQuerySubGraph(@RequestBody Map params) {
        Integer k = Integer.parseInt(params.get("number").toString());
        ComSubGraph comSubGraph = ComSubGraphGetter.getSubComGraphs((Integer) params.get("pageNum"), (Integer) params.get("pageSize"), (Boolean) params.get("tag"), k);
        return InfoDTO.success().addData("comSubGraph", comSubGraph).addData("num", k);
    }

    @RequestMapping(value = "/init", method = RequestMethod.POST)
    @ResponseBody
    public InfoDTO initTitle(@RequestBody Map map) {
        LinkedHashMap<Integer, Integer> comSubGraphCounts = ComSubGraphGetter.getComSubGraphCounts();
        if (comSubGraphCounts!=null){
            return InfoDTO.success().addData("comSubGraphCounts",comSubGraphCounts);
        }else {
            return InfoDTO.fail();
        }
    }

}
