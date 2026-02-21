package com.stream.service;

import java.util.Date;
import java.util.List;
import java.math.BigDecimal;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.stream.dao.PaymentDAO;
import com.stream.dao.SubscriptionDAO;
import com.stream.dao.UserDAO;
import com.stream.bean.Payment;
import com.stream.bean.Subscription;
import com.stream.bean.User;
import com.stream.util.HibernateUtil;
import com.stream.util.PendingPaymentsException;
import com.stream.util.SubscriptionAlreadyActiveException;
import com.stream.util.ValidationException;

public class SubscriptionService {

    private final SubscriptionDAO subscriptionDAO = new SubscriptionDAO();
    private final PaymentDAO paymentDAO = new PaymentDAO();
    private final UserDAO userDAO = new UserDAO();

    public User viewUserDetails(String userID) {
        if (userID == null || userID.trim().isEmpty()) return null;
        return userDAO.findUser(userID);
    }

    public List<User> viewAllUsers() {
        return userDAO.viewAllUsers();
    }

    public boolean addNewUser(User user) {
        if (user == null) return false;

        if (user.getUserID() == null || user.getUserID().trim().isEmpty()) return false;
        if (user.getFullName() == null || user.getFullName().trim().isEmpty()) return false;
        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) return false;

        if (user.getCreatedDate() == null) user.setCreatedDate(new Date());
        if (user.getAccountStatus() == null || user.getAccountStatus().trim().isEmpty()) {
            user.setAccountStatus("ACTIVE");
        }

        if (userDAO.findUser(user.getUserID()) != null) return false;

        return userDAO.insertUser(user);
    }

    public boolean removeUser(String userID) throws PendingPaymentsException {
        if (userID == null || userID.trim().isEmpty()) return false;

        Subscription activeSubscription = subscriptionDAO.findActiveSubscriptionByUser(userID);
        List<Payment> pendingPayments = paymentDAO.findPendingPaymentsByUser(userID);

        if (activeSubscription != null || (pendingPayments != null && !pendingPayments.isEmpty())) {
            throw new PendingPaymentsException();
        }

        return userDAO.deleteUser(userID);
    }

    private void ensureUserExists(String userID) {
        User user = userDAO.findUser(userID);
        if (user != null) return;

        User u = new User();
        u.setUserID(userID);
        u.setFullName("AUTO_" + userID);
        u.setEmail(userID.toLowerCase() + "@mail.com");
        u.setPhone("0000000000");
        u.setAccountStatus("ACTIVE");
        u.setCreatedDate(new Date());
        userDAO.saveOrUpdateUser(u);
    }

    public boolean activateSubscription(String userID, String planCode, Date startDate, Date endDate, String paymentMethod)
            throws ValidationException, SubscriptionAlreadyActiveException {

        if (userID == null || userID.trim().isEmpty()
                || planCode == null || planCode.trim().isEmpty()
                || startDate == null || endDate == null
                || startDate.after(endDate)
                || paymentMethod == null || paymentMethod.trim().isEmpty()) {
            throw new ValidationException();
        }

        ensureUserExists(userID);

        Subscription active = subscriptionDAO.findActiveSubscriptionByUser(userID);
        if (active != null && planCode.equalsIgnoreCase(active.getPlanCode())) {
            throw new SubscriptionAlreadyActiveException();
        }

        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Subscription s = new Subscription();
            s.setUserID(userID);
            s.setPlanCode(planCode);
            s.setStartDate(startDate);
            s.setEndDate(endDate);
            s.setStatus("ACTIVE");
            session.persist(s);

            Payment p = new Payment();
            p.setSubscriptionID(s.getSubscriptionID());
            p.setAmount(BigDecimal.valueOf(199.00));
            p.setPaymentDate(new Date());
            p.setPaymentMethod(paymentMethod);
            p.setStatus("PENDING");
            session.persist(p);

            boolean success = simulatePayment();
            p.setStatus(success ? "SUCCESS" : "FAILED");
            session.merge(p);

            if (!success) {
                tx.rollback();
                return false;
            }

            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean renewSubscription(int subscriptionID, Date newEndDate, String paymentMethod)
            throws ValidationException {

        if (subscriptionID <= 0 || newEndDate == null
                || paymentMethod == null || paymentMethod.trim().isEmpty()) {
            throw new ValidationException();
        }

        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Subscription s = session.get(Subscription.class, subscriptionID);
            if (s == null) {
                tx.rollback();
                return false;
            }

            if (!newEndDate.after(s.getEndDate())) {
                throw new ValidationException();
            }

            Payment p = new Payment();
            p.setSubscriptionID(subscriptionID);
            p.setAmount(BigDecimal.valueOf(199.00));
            p.setPaymentDate(new Date());
            p.setPaymentMethod(paymentMethod);
            p.setStatus("PENDING");
            session.persist(p);

            boolean success = simulatePayment();
            p.setStatus(success ? "SUCCESS" : "FAILED");
            session.merge(p);

            if (!success) {
                tx.rollback();
                return false;
            }

            s.setEndDate(newEndDate);
            s.setStatus("ACTIVE");
            session.merge(s);

            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    public boolean processPayment(int paymentID, double amount, String paymentMethod)
            throws ValidationException {
        return processPayment(amount, paymentMethod);
    }

    public boolean processPayment(double amount, String paymentMethod)
            throws ValidationException {

        if (amount <= 0 || paymentMethod == null || paymentMethod.trim().isEmpty()) {
            throw new ValidationException();
        }

        Transaction tx = null;
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            tx = session.beginTransaction();

            Subscription subscription = subscriptionDAO.findLatestSubscription();
            if (subscription == null) {
                tx.rollback();
                return false;
            }

            Payment p = new Payment();
            p.setSubscriptionID(subscription.getSubscriptionID());
            p.setAmount(BigDecimal.valueOf(amount));
            p.setPaymentDate(new Date());
            p.setPaymentMethod(paymentMethod);
            p.setStatus("PENDING");
            session.persist(p);

            boolean success = simulatePayment();
            p.setStatus(success ? "SUCCESS" : "FAILED");
            session.merge(p);

            if (!success) {
                tx.rollback();
                return false;
            }

            tx.commit();
            return true;

        } catch (Exception e) {
            if (tx != null) tx.rollback();
            e.printStackTrace();
            return false;
        }
    }

    private boolean simulatePayment() {
        return true;
    }
}