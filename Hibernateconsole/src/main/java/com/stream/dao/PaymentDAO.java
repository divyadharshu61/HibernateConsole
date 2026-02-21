package com.stream.dao;

import java.util.Collections;
import java.util.List;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.stream.bean.Payment;
import com.stream.util.HibernateUtil;

public class PaymentDAO {

    public boolean recordPayment(Payment pay) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            session.persist(pay);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public List<Payment> findPendingPaymentsByUser(String userID) {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            return session.createQuery("SELECT p FROM Payment p, Subscription s WHERE p.subscriptionID = s.subscriptionID AND s.userID = :uid AND p.status = 'PENDING'", Payment.class ).setParameter("uid", userID).getResultList();
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    public boolean updatePaymentStatus(long paymentID, String status) {
        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();
            Payment p = session.get(Payment.class, paymentID);
            if (p == null) return false;
            p.setStatus(status);
            session.merge(p);
            tx.commit();
            return true;
        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }
}