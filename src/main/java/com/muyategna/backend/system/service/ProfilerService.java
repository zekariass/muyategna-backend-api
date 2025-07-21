//package com.muyategna.backend.system.service;
//
//import jakarta.persistence.EntityManagerFactory;
//import org.hibernate.SessionFactory;
//import org.hibernate.stat.Statistics;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class ProfilerService {
//
//    @Autowired
//    private EntityManagerFactory entityManagerFactory;
//
//    public void logDbHits() {
//        SessionFactory sessionFactory = entityManagerFactory.unwrap(SessionFactory.class);
//        Statistics stats = sessionFactory.getStatistics();
//
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Queries executed: " + stats.getQueryExecutionCount());
//        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Entity fetches: " + stats.getEntityFetchCount());
//    }
//}
