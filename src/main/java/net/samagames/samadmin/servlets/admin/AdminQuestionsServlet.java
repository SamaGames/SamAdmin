package net.samagames.samadmin.servlets.admin;

import javafx.util.Pair;
import net.samagames.samadmin.servlets.AbstractServlet;
import net.samagames.samadmin.users.User;
import net.samagames.samadmin.users.UserPermissionLevel;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/*
 * This file is part of SamAdmin.
 *
 * SamAdmin is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * SamAdmin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with SamAdmin.  If not, see <http://www.gnu.org/licenses/>.
 */
@WebServlet(
        name = "AdminQuestionsServlet",
        value = {
                "/admin/faq"
        }
)
public class AdminQuestionsServlet extends AbstractServlet
{
    @Override
    public void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        if (!this.instance.getUserManager().isLogged(request.getSession()))
        {
            this.redirectTo(response, "login");
            return;
        }

        if (!this.instance.getUserManager().isAdmin(request.getSession()))
        {
            this.redirectTo(response, "dashboard");
            return;
        }

        request.setAttribute("title", "Gestion des questions");
        request.setAttribute("base-url", "../");
        request.setAttribute("questions", this.instance.getQuestionManager().getCache().getCachedObjects().values());
        request.getRequestDispatcher("../www/admin/faq.jsp").forward(request, response);
    }

    @Override
    public void actionGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        this.processRequest(request, response);
    }

    @Override
    public void actionPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
    {
        if (request.getParameter("action") != null)
        {
            String action = request.getParameter("action");

            if (action.equals("create") && request.getParameter("question") != null && request.getParameter("answer") != null && UserPermissionLevel.moreThan(this.instance.getUserManager().getActual(request.getSession()).getPermissionLevel(), UserPermissionLevel.ADMINISTRATOR))
            {
                String question = request.getParameter("question");
                String answer = request.getParameter("answer");

                byte[] questionBytes = question.getBytes(StandardCharsets.ISO_8859_1);
                question = new String(questionBytes, StandardCharsets.UTF_8);

                byte[] answerBytes = answer.getBytes(StandardCharsets.ISO_8859_1);
                answer = new String(answerBytes, StandardCharsets.UTF_8);

                StringBuilder questionBuilder = new StringBuilder(question);

                int loc = new String(questionBuilder).indexOf('\n');

                while (loc > 0)
                {
                    questionBuilder.replace(loc, (loc + 1), "<br/>");
                    loc = new String(questionBuilder).indexOf('\n');
                }

                StringBuilder answerBuilder = new StringBuilder(answer);

                loc = new String(answerBuilder).indexOf('\n');

                while (loc > 0)
                {
                    answerBuilder.replace(loc, (loc + 1), "<br/>");
                    loc = new String(answerBuilder).indexOf('\n');
                }

                Pair<Boolean, String> result = this.instance.getQuestionManager().addQuestion(questionBuilder.toString(), answerBuilder.toString());

                if (!result.getKey())
                    request.setAttribute("error", result.getValue());
            }
            else if (action.equals("edit") && request.getParameter("question") != null && request.getParameter("question") != null && request.getParameter("answer") != null && UserPermissionLevel.moreThan(this.instance.getUserManager().getActual(request.getSession()).getPermissionLevel(), UserPermissionLevel.ADMINISTRATOR))
            {
                int questionID = Integer.valueOf(request.getParameter("question"));
                String question = request.getParameter("question");
                String answer = request.getParameter("answer");

                byte[] questionBytes = question.getBytes(StandardCharsets.ISO_8859_1);
                question = new String(questionBytes, StandardCharsets.UTF_8);

                byte[] answerBytes = answer.getBytes(StandardCharsets.ISO_8859_1);
                answer = new String(answerBytes, StandardCharsets.UTF_8);

                StringBuilder questionBuilder = new StringBuilder(question);

                int loc = new String(questionBuilder).indexOf('\n');

                while (loc > 0)
                {
                    questionBuilder.replace(loc, (loc + 1), "<br/>");
                    loc = new String(questionBuilder).indexOf('\n');
                }

                StringBuilder answerBuilder = new StringBuilder(answer);

                loc = new String(answerBuilder).indexOf('\n');

                while (loc > 0)
                {
                    answerBuilder.replace(loc, (loc + 1), "<br/>");
                    loc = new String(answerBuilder).indexOf('\n');
                }

                Pair<Boolean, String> result = this.instance.getQuestionManager().editQuestion(questionID, questionBuilder.toString(), answerBuilder.toString());

                if (!result.getKey())
                    request.setAttribute("error", result.getValue());
            }
            else if (action.equals("delete") && request.getParameter("question") != null && UserPermissionLevel.moreThan(this.instance.getUserManager().getActual(request.getSession()).getPermissionLevel(), UserPermissionLevel.ADMINISTRATOR))
            {
                int questionID = Integer.valueOf(request.getParameter("question"));

                Pair<Boolean, String> result = this.instance.getQuestionManager().removeQuestion(questionID);

                if (!result.getKey())
                    request.setAttribute("error", result.getValue());
            }
        }

        this.processRequest(request, response);
    }
}
