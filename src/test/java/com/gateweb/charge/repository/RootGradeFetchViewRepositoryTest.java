package com.gateweb.charge.repository;

import com.gateweb.charge.config.SpringWebMvcConfig;
import com.gateweb.orm.charge.entity.RootGradeFetchView;
import com.gateweb.charge.chargePolicy.grade.service.RootGradeSearchService;
import com.google.gson.Gson;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.List;

/**
 * Created by Eason on 3/28/2018.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {SpringWebMvcConfig.class})
@WebAppConfiguration
public class RootGradeFetchViewRepositoryTest {

    @Autowired
    RootGradeSearchService rootGradeSearchService;

    Gson gson = new Gson();

    @Test
    public void rootGradeFetchViewListTest() {
        List<RootGradeFetchView> rootGradeFetchViewList = rootGradeSearchService.findAllRootGradeFetchView();
        rootGradeFetchViewList.stream().forEach(rootGradeFetchView -> {
            System.out.println(gson.toJson(rootGradeFetchView));
        });
    }
}
