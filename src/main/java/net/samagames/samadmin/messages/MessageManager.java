package net.samagames.samadmin.messages;

import javafx.util.Pair;
import net.samagames.samadmin.SamAdmin;
import net.samagames.samadmin.utils.JsonGlobalMessage;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.*;

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
public class MessageManager
{
    private final SamAdmin instance;
    private final ScheduledExecutorService executor;
    private final ConcurrentHashMap<Integer, Pair<Message, ScheduledFuture>> messages;

    public MessageManager(SamAdmin instance)
    {
        this.instance = instance;
        this.executor = Executors.newScheduledThreadPool(12);
        this.messages = new ConcurrentHashMap<>();

        this.reloadMessages();
    }

    public void reloadMessages()
    {
        if (!this.messages.isEmpty())
        {
            this.messages.keySet().forEach(this::cancelSchedulingMessage);
            this.messages.clear();
        }

        this.getMessages().forEach(this::scheduleMessage);
    }

    public void scheduleMessage(Message message)
    {
        this.messages.put(message.getID(), new Pair<>(message, this.executor.scheduleAtFixedRate(() ->
                new JsonGlobalMessage(message.getFrom(), message.getMessage()).send(), message.getDelay(), message.getDelay(), message.getTimeUnit())));
    }

    public void cancelSchedulingMessage(int id)
    {
        if (this.messages.containsKey(id))
        {
            this.messages.get(id).getValue().cancel(true);
            this.messages.remove(id);
        }
    }

    public Pair<Boolean, String> addMessage(String from, String message, int delay, TimeUnit timeUnit)
    {
        try (Connection sql = this.instance.getMySQLAgent().openApplicationConnection())
        {
            PreparedStatement pStatement = sql.prepareStatement("INSERT INTO sam_messages (`fromname`, `message`, `delay`, `timeunit`) VALUES (?, ?, ?, ?)");
            pStatement.setString(1, from);
            pStatement.setString(2, message);
            pStatement.setInt(3, delay);
            pStatement.setString(4, timeUnit.name());
            pStatement.executeUpdate();

            sql.close();

            this.reloadMessages();

            return new Pair<>(true, null);
        }
        catch (SQLException e)
        {
            return new Pair<>(false, e.getMessage());
        }
    }

    public Pair<Boolean, String> editMessage(int id, String from, String message, int delay, TimeUnit timeUnit)
    {
        try (Connection sql = this.instance.getMySQLAgent().openApplicationConnection())
        {
            PreparedStatement pStatement = sql.prepareStatement("UPDATE sam_messages SET fromname = ?, message = ?, delay = ?, timeunit = ? WHERE id = ?");
            pStatement.setString(1, from);
            pStatement.setString(2, message);
            pStatement.setInt(3, delay);
            pStatement.setString(4, timeUnit.name());
            pStatement.setInt(5, id);
            pStatement.executeUpdate();

            sql.close();

            this.reloadMessages();

            return new Pair<>(true, null);
        }
        catch (SQLException e)
        {
            return new Pair<>(false, e.getMessage());
        }
    }

    public Pair<Boolean, String> removeMessage(int id)
    {
        try (Connection sql = this.instance.getMySQLAgent().openApplicationConnection())
        {
            PreparedStatement pStatement = sql.prepareStatement("SELECT * FROM sam_messages WHERE id = ?");
            pStatement.setInt(1, id);

            ResultSet result = pStatement.executeQuery();

            if(!result.next())
                return new Pair<>(false, "Message non trouvé dans la base de données !");

            pStatement = sql.prepareStatement("DELETE FROM sam_messages WHERE id = ?");
            pStatement.setInt(1, id);
            pStatement.executeUpdate();

            sql.close();

            this.reloadMessages();

            return new Pair<>(true, null);
        }
        catch (SQLException e)
        {
            return new Pair<>(false, e.getMessage());
        }
    }

    public ArrayList<Message> getMessages()
    {
        ArrayList<Message> temp = new ArrayList<>();

        try (Connection sql = this.instance.getMySQLAgent().openApplicationConnection())
        {
            PreparedStatement pStatement = sql.prepareStatement("SELECT * FROM sam_messages");
            ResultSet result = pStatement.executeQuery();

            while(result.next())
                temp.add(new Message(result));

            sql.close();
        }
        catch (SQLException ignored) {}

        return temp;
    }
}
