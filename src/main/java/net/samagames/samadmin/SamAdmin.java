package net.samagames.samadmin;

import net.samagames.samadmin.groups.GroupManager;
import net.samagames.samadmin.messages.MessageManager;
import net.samagames.samadmin.players.PlayerManager;
import net.samagames.samadmin.questions.QuestionManager;
import net.samagames.samadmin.statistics.StatisticManager;
import net.samagames.samadmin.users.UserManager;
import net.samagames.samadmin.utils.ChatColor;
import net.samagames.samadmin.utils.JsonModMessage;
import net.samagames.samadmin.utils.ModChannel;
import net.samagames.samadmin.utils.MySQLAgent;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.servlet.ServletContextEvent;
import java.util.logging.Level;
import java.util.logging.Logger;

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
public class SamAdmin
{
    private static SamAdmin instance;

    private final Configuration configuration;
    private final MySQLAgent mySQLAgent;
    private final JedisPool jedisPool;

    private final UserManager userManager;
    private final QuestionManager questionManager;
    private final PlayerManager playerManager;
    private final GroupManager groupManager;
    private final MessageManager messageManager;
    private final StatisticManager statisticManager;

    public SamAdmin(ServletContextEvent sce)
    {
        instance = this;

        System.out.println("================================================");
        System.out.println("=[                 Welcome to                 ]=");
        System.out.println("=[                  SamAdmin                  ]=");
        System.out.println("================================================");

        this.configuration = new Configuration(sce);
        this.mySQLAgent = new MySQLAgent(this);

        JedisPoolConfig jedisConfiguration = new JedisPoolConfig();
        jedisConfiguration.setMaxTotal(1024);
        jedisConfiguration.setMaxWaitMillis(5000);

        Logger logger = Logger.getLogger(JedisPool.class.getName());
        logger.setLevel(Level.OFF);

        this.jedisPool = new JedisPool(jedisConfiguration, this.configuration.REDIS_HOST, Integer.valueOf(this.configuration.REDIS_PORT), 5000, this.configuration.REDIS_PASSWORD);

        this.userManager = new UserManager(this);
        this.questionManager = new QuestionManager(this);
        this.playerManager = new PlayerManager(this);
        this.groupManager = new GroupManager(this);
        this.messageManager = new MessageManager(this);
        this.statisticManager = new StatisticManager(this);

        new JsonModMessage("SamAdmin", ModChannel.INFORMATION, ChatColor.RED, "SamAdmin est prêt à être utilisé !").send();
    }

    public void shutdown()
    {
        new JsonModMessage("SamAdmin", ModChannel.INFORMATION, ChatColor.RED, "Arrêt ! Je reviens bientot ! :'(").send();
    }

    public Configuration getConfiguration()
    {
        return this.configuration;
    }

    public MySQLAgent getMySQLAgent()
    {
        return this.mySQLAgent;
    }

    public JedisPool getJedisPool()
    {
        return this.jedisPool;
    }

    public UserManager getUserManager()
    {
        return this.userManager;
    }

    public QuestionManager getQuestionManager()
    {
        return this.questionManager;
    }

    public PlayerManager getPlayerManager()
    {
        return this.playerManager;
    }

    public GroupManager getGroupManager()
    {
        return this.groupManager;
    }

    public MessageManager getMessageManager()
    {
        return this.messageManager;
    }

    public StatisticManager getStatisticManager()
    {
        return this.statisticManager;
    }

    public static void init(ServletContextEvent sce)
    {
        if (instance == null)
            new SamAdmin(sce);
    }

    public static SamAdmin getInstance()
    {
        return instance;
    }
}
