package com.android.sagot.mynews;

import com.android.sagot.mynews.Models.Criteria;
import com.android.sagot.mynews.Utils.DateUtilities;
import com.android.sagot.mynews.Utils.NYTimesRequest;

import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

import static org.junit.Assert.*;

/**
 *   Unit Test of the app MyNews
 */
public class DateUtilities_Unit_Test {

    @Test
    public void dateReformatSSAAMMJJ_Unit_Test() {
        String originDate = "2018-05-08T12:37:10-04:00";         //Original date to reformat
        assertEquals("20180508", DateUtilities.dateReformatSSAAMMJJ(originDate));
    }

    @Test
    public void dateReformat_Unit_Test() {
        String originDate = "20180508";         //Original date to reformat
        assertEquals("08/05/18", DateUtilities.dateReformat(originDate));
    }

    @Test
    public void createQuery_sort_Unit_Test() {
        Criteria criteria = new Criteria();
        criteria.setBusiness(true);
        criteria.setPolitics(true);
        criteria.setArts(true);
        criteria.setEntrepreneurs(true);
        criteria.setSports(true);
        criteria.setTravel(true);
        criteria.setKeysWords("World Cup");
        NYTimesRequest request = new NYTimesRequest();
        request.createQuery(criteria);

        assertEquals("newest", request.getQuery().get("sort"));
        assertEquals("World Cup", request.getQuery().get("q"));
        assertEquals("news_desk:( \"Arts\" \"Business\" " +
                "\"Entrepreneurs\" \"Politics\" \"Sports\" \"Travel\")", request.getQuery().get("fq"));

        criteria.setBusiness(false);
        criteria.setPolitics(false);
        criteria.setArts(false);
        request.createQuery(criteria);
        assertEquals("news_desk:( \"Entrepreneurs\" \"Sports\" \"Travel\")", request.getQuery().get("fq"));
    }

    @Test
    public void createQuery_q_Unit_Test() {
        Criteria criteria = new Criteria();
        criteria.setKeysWords("World Cup");
        NYTimesRequest request = new NYTimesRequest();
        request.createQuery(criteria);

        assertEquals("World Cup", request.getQuery().get("q"));
    }

    @Test
    public void createQuery_fq_Unit_Test() {
        Criteria criteria = new Criteria();
        criteria.setBusiness(true);
        criteria.setPolitics(true);
        criteria.setArts(true);
        criteria.setEntrepreneurs(true);
        criteria.setSports(true);
        criteria.setTravel(true);
        NYTimesRequest request = new NYTimesRequest();
        request.createQuery(criteria);

        assertEquals("news_desk:( \"Arts\" \"Business\" " +
                "\"Entrepreneurs\" \"Politics\" \"Sports\" \"Travel\")", request.getQuery().get("fq"));
    }

    @Test
    public void createQuery_addDate_Unit_Test() {
        NYTimesRequest request = new NYTimesRequest();
        request.createQuery(new Criteria());
        request.addDateCriteriaToQuery(new Date(),"beginDate");
        SimpleDateFormat criteriaDateFormatter = new SimpleDateFormat("yyyyMMdd", Locale.US);

        assertEquals(criteriaDateFormatter.format(new Date()), request.getQuery().get("beginDate"));
    }
}