package com.realestatefinder.servlet.admin;

import com.realestatefinder.model.Agent;
import com.realestatefinder.service.AgentService;
import com.realestatefinder.util.ServletUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/admin/agents")
public class AdminAgentServlet extends AdminBaseServlet {
    private final AgentService agentService = new AgentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        if (!ensureAdmin(request, response)) {
            return;
        }

        try {
            String editId = request.getParameter("editId");
            if (editId != null && !editId.isBlank()) {
                agentService.findById(Integer.parseInt(editId)).ifPresent(agent -> request.setAttribute("editAgent", agent));
            }
            request.setAttribute("agents", agentService.findAll());
            request.getRequestDispatcher("/WEB-INF/views/admin-agents.jsp").forward(request, response);
        } catch (SQLException e) {
            throw new ServletException("Failed to load agents.", e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        if (!ensureAdmin(request, response)) {
            return;
        }
        request.setCharacterEncoding("UTF-8");
        String action = ServletUtil.safeTrim(request.getParameter("action"));

        try {
            if ("delete".equalsIgnoreCase(action)) {
                int id = Integer.parseInt(request.getParameter("id"));
                agentService.delete(id, getActor(request));
                ServletUtil.redirectWithMessage(request, response, "/admin/agents", "success", "Agent deleted successfully.");
                return;
            }

            Agent agent = new Agent();
            agent.setId(ServletUtil.parseInt(request.getParameter("id"), 0));
            agent.setFullName(request.getParameter("fullName"));
            agent.setEmail(request.getParameter("email"));
            agent.setPhone(request.getParameter("phone"));
            agent.setCity(request.getParameter("city"));
            agent.setSpecialty(request.getParameter("specialty"));
            agent.setExperienceYears(ServletUtil.parseInt(request.getParameter("experienceYears"), 0));
            agent.setRating(ServletUtil.parseDouble(request.getParameter("rating"), 4.5));
            agent.setStatus(request.getParameter("status"));
            agent.setBio(request.getParameter("bio"));

            agentService.save(agent, getActor(request));
            ServletUtil.redirectWithMessage(request, response, "/admin/agents", "success", "Agent saved successfully.");
        } catch (SQLException e) {
            throw new ServletException("Failed to save agent.", e);
        }
    }
}
