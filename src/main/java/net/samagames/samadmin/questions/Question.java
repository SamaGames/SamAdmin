package net.samagames.samadmin.questions;

import java.sql.ResultSet;
import java.sql.SQLException;

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
public class Question
{
    private final int id;
    private final String question;
    private final String answer;

    public Question(ResultSet resultSet) throws SQLException
    {
        this.id = resultSet.getInt("id");
        this.question = resultSet.getString("question");
        this.answer = resultSet.getString("answer");
    }

    public int getID()
    {
        return this.id;
    }

    public String getQuestion()
    {
        return this.question;
    }

    public String getAnswer()
    {
        return this.answer;
    }
}
