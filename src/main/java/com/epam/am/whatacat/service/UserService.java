package com.epam.am.whatacat.service;

import com.epam.am.whatacat.dao.DaoException;
import com.epam.am.whatacat.dao.UserDao;
import com.epam.am.whatacat.dao.jdbc.JdbcUserDao;
import com.epam.am.whatacat.model.User;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Random;

public class UserService extends BaseService {
    public UserService() throws ServiceException {
    }

    public User register(User user, String password) throws ServiceException {
        try {
            user.setHashedPassword(hashPassword(password));
            UserDao userDao = daoFactory.getUserDao();
            return userDao.save(user);
        } catch (DaoException e) {
            throw new ServiceException("Unable to register user", e);
        }
    }

    public User logIn(String email, String password) throws ServiceException {
        try {
            UserDao userDao = daoFactory.getUserDao();
            String hashedPassword = hashPassword(password);
            return userDao.findByEmailAndPassword(email, hashedPassword);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public boolean isEmailFree(String email) throws ServiceException {
        try {
            UserDao userDao = daoFactory.getUserDao();
            return userDao.isEmailFree(email);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    public boolean isNicknameFree(String nickname) throws ServiceException {
        try {
            UserDao userDao = daoFactory.getUserDao();
            return userDao.isNicknameFree(nickname);
        } catch (DaoException e) {
            throw new ServiceException(e);
        }
    }

    private String hashPassword(String password) throws ServiceException {
        try {
            byte[] salt = new byte[]{-76, 123, -56, -17, 21, -114, -91, 1, 73, 60, 95, -65, -23, 112, -98, 16};
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = f.generateSecret(spec).getEncoded();
            Base64.Encoder enc = Base64.getEncoder();
            return enc.encodeToString(hash);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
            throw new ServiceException(e);
        }
    }
}
