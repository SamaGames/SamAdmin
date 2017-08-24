package net.samagames.samadmin.users;

import javafx.util.Pair;
import net.samagames.samadmin.SamAdmin;
import net.samagames.samadmin.utils.CryptUtils;

import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.UUID;

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
public class UserManager
{
    private final SamAdmin instance;

    public UserManager(SamAdmin instance)
    {
        this.instance = instance;
    }

    public User getActual(HttpSession session)
    {
        return (User) session.getAttribute("user-object");
    }

    public Pair<Boolean, String> addUser(String username, String password, UUID uuid, UserPermissionLevel permission)
    {
        try (Connection sql = this.instance.getMySQLAgent().openApplicationConnection())
        {
            PreparedStatement pStatement = sql.prepareStatement("SELECT * FROM sam_users WHERE username = ?");
            pStatement.setString(1, username);

            ResultSet result = pStatement.executeQuery();

            if(result.next() && result.getString("username") != null)
                return new Pair<>(false, "Nom d'utilisateur déjà utilisé !");

            pStatement = sql.prepareStatement("INSERT INTO sam_users (`username`, `password`, `uuid`, `permission`) VALUES (?, ?, ?, ?)");
            pStatement.setString(1, username);
            pStatement.setString(2, CryptUtils.toSHA256(this.instance.getConfiguration().SALT + password));
            pStatement.setString(3, uuid.toString());
            pStatement.setString(4, permission.name());
            pStatement.executeUpdate();

            sql.close();

            return new Pair<>(true, null);
        }
        catch (SQLException e)
        {
            return new Pair<>(false, e.getMessage());
        }
    }

    public Pair<Boolean, String> editUser(int id, String username, String password, UUID uuid, UserPermissionLevel permission)
    {
        try (Connection sql = this.instance.getMySQLAgent().openApplicationConnection())
        {
            PreparedStatement pStatement = sql.prepareStatement("UPDATE sam_users SET username = ?, uuid = ?, permission = ? WHERE id = ?");
            pStatement.setString(1, username);
            pStatement.setString(2, uuid.toString());
            pStatement.setString(3, permission.name());
            pStatement.setInt(4, id);
            pStatement.executeUpdate();

            sql.close();

            if (password != null)
            {
                Pair<Boolean, String> result = this.editPassword(id, password);

                if (!result.getKey())
                    return new Pair<>(false, result.getValue());
            }

            return new Pair<>(true, null);
        }
        catch (SQLException e)
        {
            return new Pair<>(false, e.getMessage());
        }
    }

    public Pair<Boolean, String> editPassword(int id, String password)
    {
        try (Connection sql = this.instance.getMySQLAgent().openApplicationConnection())
        {
            PreparedStatement pStatement = sql.prepareStatement("UPDATE sam_users SET password = ? WHERE id = ?");
            pStatement.setString(1, CryptUtils.toSHA256(this.instance.getConfiguration().SALT + password));
            pStatement.setInt(2, id);
            pStatement.executeUpdate();

            sql.close();

            return new Pair<>(true, null);
        }
        catch (SQLException e)
        {
            return new Pair<>(false, e.getMessage());
        }
    }

    public Pair<Boolean, String> removeUser(int id)
    {
        try (Connection sql = this.instance.getMySQLAgent().openApplicationConnection())
        {
            PreparedStatement pStatement = sql.prepareStatement("SELECT * FROM sam_users WHERE id = ?");
            pStatement.setInt(1, id);

            ResultSet result = pStatement.executeQuery();

            if(!result.next())
                return new Pair<>(false, "Utilisateur non trouvé dans la base de données !");

            pStatement = sql.prepareStatement("DELETE FROM sam_users WHERE id = ?");
            pStatement.setInt(1, id);
            pStatement.executeUpdate();

            sql.close();

            return new Pair<>(true, null);
        }
        catch (SQLException e)
        {
            return new Pair<>(false, e.getMessage());
        }
    }

    public User getUserByID(int id)
    {
        try (Connection sql = this.instance.getMySQLAgent().openApplicationConnection())
        {
            PreparedStatement pStatement = sql.prepareStatement("SELECT * FROM sam_users WHERE id = ?");
            pStatement.setInt(1, id);

            ResultSet result = pStatement.executeQuery();

            User user = null;

            if(result.next())
                user =  new User(result);

            sql.close();

            return user;
        }
        catch (SQLException ignored)
        {
            return null;
        }
    }

    public ArrayList<User> getUserList()
    {
        ArrayList<User> temp = new ArrayList<>();

        try (Connection sql = this.instance.getMySQLAgent().openApplicationConnection())
        {
            PreparedStatement pStatement = sql.prepareStatement("SELECT * FROM sam_users");
            ResultSet result = pStatement.executeQuery();

            while(result.next())
                temp.add(new User(result));

            sql.close();
        }
        catch (SQLException ignored) {}

        return temp;
    }

    public boolean isLogged(HttpSession session)
    {
        return session.getAttribute("user-object") != null;
    }

    public boolean isAdmin(HttpSession session)
    {
        return (this.isLogged(session) && ((User) session.getAttribute("user-object")).getPermissionLevel() == UserPermissionLevel.ADMINISTRATOR);
    }
}
