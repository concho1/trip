package com.goott.trip.jhm.mapper;

import com.goott.trip.jhm.model.FAQ;
import com.goott.trip.jhm.model.Page;
import com.goott.trip.jhm.model.QNA;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface QNAMapper {

    public int insertFAQ(FAQ fdto);
    public List<FAQ> getFAQByDiv(String div);
    public int getQNACount();
    public List<QNA> getQNAList(Page pdto);
    public int insertQNA(QNA qdto);

}
