package net.samagames.samadmin.questions;

import javafx.util.Pair;
import net.samagames.samadmin.SamAdmin;
import net.samagames.samadmin.common.AbstractObjectManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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
public class QuestionManager extends AbstractObjectManager<QuestionCache>
{
    public QuestionManager(SamAdmin instance)
    {
        super(instance, QuestionCache.class);
    }

    public Pair<Boolean, String> addQuestion(String question, String answer)
    {
        try (Connection sql = this.instance.getMySQLAgent().openApplicationConnection())
        {
            PreparedStatement pStatement = sql.prepareStatement("INSERT INTO sam_questions (`question`, `answer`) VALUES (?, ?)");
            pStatement.setString(1, question);
            pStatement.setString(2, answer);
            pStatement.executeUpdate();

            sql.close();

            this.getCache().reload();

            return new Pair<>(true, null);
        }
        catch (SQLException e)
        {
            return new Pair<>(false, e.getMessage());
        }
    }

    public Pair<Boolean, String> editQuestion(int id, String question, String answer)
    {
        try (Connection sql = this.instance.getMySQLAgent().openApplicationConnection())
        {
            PreparedStatement pStatement = sql.prepareStatement("UPDATE sam_questions SET question = ?, answer = ? WHERE id = ?");
            pStatement.setString(1, question);
            pStatement.setString(2, answer);
            pStatement.setInt(3, id);
            pStatement.executeUpdate();

            sql.close();

            this.getCache().reload();

            return new Pair<>(true, null);
        }
        catch (SQLException e)
        {
            return new Pair<>(false, e.getMessage());
        }
    }

    public Pair<Boolean, String> removeQuestion(int id)
    {
        try (Connection sql = this.instance.getMySQLAgent().openApplicationConnection())
        {
            PreparedStatement pStatement = sql.prepareStatement("DELETE FROM sam_questions WHERE id = ?");
            pStatement.setInt(1, id);
            pStatement.executeUpdate();

            sql.close();

            this.getCache().reload();

            return new Pair<>(true, null);
        }
        catch (SQLException e)
        {
            return new Pair<>(false, e.getMessage());
        }
    }
}
