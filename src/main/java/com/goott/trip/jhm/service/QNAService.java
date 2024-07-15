package com.goott.trip.jhm.service;

import com.goott.trip.jhm.mapper.QNAMapper;
import com.goott.trip.jhm.model.FAQ;
import com.goott.trip.jhm.model.Page;
import com.goott.trip.jhm.model.QNA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QNAService {

    @Autowired
    private QNAMapper mapper;

    public int insertFAQ(FAQ fdto) { return this.mapper.insertFAQ(fdto);}
    public List<FAQ> getFAQByDiv(String div) { return this.mapper.getFAQByDiv(div); }
    public int getQNACount() { return this.mapper.getQNACount(); }
    public List<QNA> getQNAList(Page pdto) { return this.mapper.getQNAList(pdto); }
    public int insertQNA(QNA qdto) { return this.mapper.insertQNA(qdto); }
    public String findRole(String id) { return this.mapper.findRole(id); }
    public QNA getQNAContent(int num) { return this.mapper.getQNAContent(num); }
    public int modQNA(QNA qdto) { return this.mapper.modQNA(qdto); }
    public int delQNA(int no) { return this.mapper.delQNA(no); }
    public void updateSeq(int no) { this.mapper.updateSeq(no); }
    public int answerQNA(QNA qdto) { return this.mapper.answerQNA(qdto); }
    public void changeStatus(int no) { this.mapper.changeStatus(no); }
    public String getAnswer(int no) { return this.mapper.getAnswer(no); }

}
