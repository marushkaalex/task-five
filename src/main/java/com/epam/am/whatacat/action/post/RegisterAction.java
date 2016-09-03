package com.epam.am.whatacat.action.post;

import com.epam.am.whatacat.action.Action;
import com.epam.am.whatacat.action.ActionException;
import com.epam.am.whatacat.action.ActionResult;
import com.epam.am.whatacat.model.Gender;
import com.epam.am.whatacat.model.Role;
import com.epam.am.whatacat.model.User;
import com.epam.am.whatacat.service.ServiceException;
import com.epam.am.whatacat.service.UserService;
import com.epam.am.whatacat.validation.FormValidator;
import com.epam.am.whatacat.validation.FormValidatorFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class RegisterAction implements Action {
    private static final Logger log = LoggerFactory.getLogger(RegisterAction.class);

    @Override
    public ActionResult execute(HttpServletRequest request, HttpServletResponse response) throws ActionException {
        try {
            FormValidator validator = FormValidatorFactory.getInstance().getValidator("register");
            List<String> errorList = validator.validate(request.getParameterMap());
            if (!errorList.isEmpty()) {
                request.setAttribute("errorList", errorList);
                return new ActionResult("register");
            }

            String email = request.getParameter("email");
            String nickname = request.getParameter("nickname");
            String password = request.getParameter("password");

            User user = new User();
            user.setEmail(email);
            user.setNickname(nickname);
            user.setGender(Gender.UNDEFINED);
            user.setRole(Role.USER);
            user.setRegistrationDate(new Date());
            user.setHashedPassword(hashPassword(password));

            UserService userService = new UserService();
            userService.register(user);

            log.info("User {} <{}> has been registered", nickname, email);
            return new ActionResult("index", true);
        } catch (ServiceException e) {
            throw new ActionException(e);
        }
    }

    private String hashPassword(String password) {
        try {
            byte[] salt = new byte[16];
            new Random().nextBytes(salt);
            KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
            SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
            byte[] hash = f.generateSecret(spec).getEncoded();
            Base64.Encoder enc = Base64.getEncoder();
            return enc.encodeToString(hash);
        } catch (InvalidKeySpecException | NoSuchAlgorithmException ignored) {
            return null;
        }
    }
}
