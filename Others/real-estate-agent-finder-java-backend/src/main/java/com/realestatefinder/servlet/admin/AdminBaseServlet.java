package com.realestatefinder.servlet.admin;

import com.realestatefinder.model.AbstractUser;
import com.realestatefinder.model.AdminUser;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public abstract class AdminBaseServlet extends HttpServlet {
    protected boolean ensureAdmin(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Object sessionUser = request.getSession().getAttribute("loggedInUser");
        if (!(sessionUser instanceof AbstractUser) || !((AbstractUser) sessionUser).isAdmin()) {
            response.sendRedirect(request.getContextPath() + "/auth?mode=login");
            return false;
        }
        return true;
    }

    protected String getActor(HttpServletRequest request) {
        Object sessionUser = request.getSession().getAttribute("loggedInUser");
        if (sessionUser instanceof AdminUser) {
            return ((AdminUser) sessionUser).getEmail();
        }
        if (sessionUser instanceof AbstractUser) {
            return ((AbstractUser) sessionUser).getEmail();
        }
        return "unknown-admin";
    }
}
