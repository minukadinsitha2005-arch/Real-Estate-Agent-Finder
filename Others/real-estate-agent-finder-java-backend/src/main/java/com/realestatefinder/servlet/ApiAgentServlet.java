package com.realestatefinder.servlet;

import com.realestatefinder.model.Agent;
import com.realestatefinder.service.AgentService;
import com.realestatefinder.util.ServletUtil;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/api/agents")
public class ApiAgentServlet extends HttpServlet {
    private final AgentService agentService = new AgentService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String keyword = request.getParameter("q");
        String city = request.getParameter("city");
        String specialty = request.getParameter("specialty");

        try {
            List<Agent> agents = agentService.search(keyword, city, specialty);
            StringBuilder json = new StringBuilder("[");
            for (int i = 0; i < agents.size(); i++) {
                Agent agent = agents.get(i);
                if (i > 0) {
                    json.append(',');
                }
                json.append('{')
                    .append("\"id\":").append(agent.getId()).append(',')
                    .append("\"fullName\":\"").append(ServletUtil.escapeJson(agent.getFullName())).append("\",")
                    .append("\"email\":\"").append(ServletUtil.escapeJson(agent.getEmail())).append("\",")
                    .append("\"phone\":\"").append(ServletUtil.escapeJson(agent.getPhone())).append("\",")
                    .append("\"city\":\"").append(ServletUtil.escapeJson(agent.getCity())).append("\",")
                    .append("\"specialty\":\"").append(ServletUtil.escapeJson(agent.getSpecialty())).append("\",")
                    .append("\"experienceYears\":").append(agent.getExperienceYears()).append(',')
                    .append("\"rating\":").append(agent.getRating()).append(',')
                    .append("\"status\":\"").append(ServletUtil.escapeJson(agent.getStatus())).append("\",")
                    .append("\"bio\":\"").append(ServletUtil.escapeJson(agent.getBio())).append("\"}");
            }
            json.append(']');
            ServletUtil.writeJson(response, json.toString());
        } catch (SQLException e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            ServletUtil.writeJson(response, "{\"error\":\"Failed to load agents.\"}");
        }
    }
}
