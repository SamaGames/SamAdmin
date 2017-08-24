<%@ page import="net.samagames.samadmin.players.Player" %>
<%@ page import="net.samagames.samadmin.players.PlayerSanction" %>
<%@ page import="org.apache.commons.lang3.text.WordUtils" %>
<%@ page import="net.samagames.samadmin.utils.UUIDTranslator" %>
<%@ page import="net.samagames.samadmin.players.PlayerStatistic" %>
<%@ page import="javafx.util.Pair" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="net.samagames.samadmin.players.PlayerShop" %>
<%@ page import="org.apache.commons.lang3.StringUtils" %>
<%@ page import="net.samagames.samadmin.groups.Group" %>
<%@ page import="java.util.Collection" %>
<%@ page import="net.samagames.samadmin.users.UserPermissionLevel" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>

<%
    String playerName = (String) request.getAttribute("player-name");
    Player player = (Player) request.getAttribute("player");

    HashMap<String, HashMap<String, Integer>> statisticsOrdered = new HashMap<>();

    for (PlayerStatistic statistic : player.getStatistics())
    {
        if (statisticsOrdered.containsKey(statistic.getCategory()))
        {
            statisticsOrdered.get(statistic.getCategory()).put(statistic.getKey(), statistic.getValue());
        }
        else
        {
            HashMap<String, Integer> gameStatistics = new HashMap<>();
            gameStatistics.put(statistic.getKey(), statistic.getValue());

            statisticsOrdered.put(statistic.getCategory(), gameStatistics);
        }
    }

    HashMap<String, HashMap<String, Pair<String[], String>>> shopsOrdored = new HashMap<>();

    for (PlayerShop shop : player.getShops())
    {
        if (shopsOrdored.containsKey(shop.getCategory()))
        {
            shopsOrdored.get(shop.getCategory()).put(shop.getKey(), new Pair<>(shop.getValue(), shop.getEquiped()));
        }
        else
        {
            HashMap<String, Pair<String[], String>> gameShops = new HashMap<>();
            gameShops.put(shop.getKey(), new Pair<>(shop.getValue(), shop.getEquiped()));

            shopsOrdored.put(shop.getCategory(), gameShops);
        }
    }
%>
<html>
    <head>
        <%@ include file="head.jsp" %>
    </head>

    <body>
        <%@ include file="sidebar.jsp" %>

        <div class="wrapper">
            <div class="constructor">
                <h2 class="headline">Profil du joueur : <%= playerName %></h2>

                <br/>

                <%
                    if (request.getAttribute("error") != null)
                    {
                %>
                <div class="alert alert-dismissable alert-danger">
                    <button type="button" class="close" data-dismiss="alert">×</button>
                    <strong>Une erreur s'est produite !</strong> <%= request.getAttribute("error") %>
                </div>

                <br/>
                <%
                    }
                %>

                <div class="player-informations-container">
                    <table class="player-informations">
                        <tr>
                            <td style="min-width: 380px" valign="top">
                                <div class="panel panel-primary">
                                    <div class="panel-heading">
                                        <h3 class="panel-title">Informations générales</h3>
                                    </div>

                                    <div class="panel-body">
                                        <br/>

                                        <img class="img-rounded center-block" src="https://minotar.net/avatar/<%= playerName %>/128" alt="Avatar" title="Avatar" />

                                        <br/><br/>

                                        <p><b>Pseudo : </b><%= playerName %></p>
                                        <p><b>UUID : </b><%= player.getPlayerUUID().toString() %></p>
                                        <p><b>Groupe : </b><%= request.getAttribute("player-group") %></p>
                                        <p><b>Dernière connexion : </b><%= player.getLastLogin().toString() %></p>

                                        <br/>

                                        <p><b style="color: #f39c12;">Pièces : </b> <%= player.getCoins() %></p>
                                        <p><b style="color: #4aa3df;">Etoiles : </b> <%= player.getStars() %></p>

                                        <%
                                            if (UserPermissionLevel.moreThan(((User) request.getSession().getAttribute("user-object")).getPermissionLevel(), UserPermissionLevel.ADMINISTRATOR))
                                            {
                                        %>
                                        <br/><br/>

                                        <div class="text-center">
                                            <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#modalGroup">Changer le groupe</button>
                                        </div>

                                        <div class="modal fade" id="modalGroup">
                                            <div class="modal-dialog">
                                                <div class="modal-content">
                                                    <form class="form-horizontal" action="player" method="post">
                                                        <fieldset>
                                                            <div class="modal-header">
                                                                <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                                                                <h4 class="modal-title">Changer le groupe</h4>
                                                            </div>

                                                            <div class="modal-body">
                                                                <div class="form-group">
                                                                    <label for="inputGroup" class="col-lg-2 control-label">Sélectionner</label>

                                                                    <div class="col-lg-10">
                                                                        <select class="form-control" name="group" id="inputGroup" required>
                                                                            <%
                                                                                for (Group group : ((Collection<Group>) request.getAttribute("groups")))
                                                                                {
                                                                            %>
                                                                            <option value="<%= group.getID() %>"><%= group.getName() %></option>
                                                                            <%
                                                                                }
                                                                            %>
                                                                        </select>
                                                                    </div>
                                                                </div>

                                                                <input type="hidden" name="action" value="group" required>
                                                                <input type="hidden" name="player-name" value="<%= playerName %>" required>
                                                                <input type="hidden" name="player" value="<%= player.getPlayerUUID().toString() %>" required>
                                                            </div>

                                                            <div class="modal-footer">
                                                                <button type="submit" class="btn btn-primary">Enregistrer</button>
                                                            </div>
                                                        </fieldset>
                                                    </form>
                                                </div>
                                            </div>
                                        </div>
                                        <%
                                            }

                                            if (UserPermissionLevel.moreThan(((User) request.getSession().getAttribute("user-object")).getPermissionLevel(), UserPermissionLevel.MASTERMODERATOR))
                                            {
                                                if (player.getGroupID() > 4)
                                                {
                                        %>
                                        <div class="text-center">
                                            <a class="btn btn-primary" href="<%= (prefix != null ? prefix : "") %>admin/moderator?name=<%= player.getPlayerName() %>">Voir le profil de modération</a>
                                        </div>
                                        <%
                                                }
                                            }
                                        %>
                                    </div>
                                </div>
                            </td>

                            <td class="space" style="width: 100%" valign="top">
                                <div>
                                    <ul class="nav nav-tabs" role="tablist">
                                        <li role="presentation" class="active"><a href="#sanctions" aria-controls="sanctions" role="tab" data-toggle="tab">Sanctions</a></li>
                                        <li role="presentation"><a href="#comments" aria-controls="comments" role="tab" data-toggle="tab">Commentaires</a></li>
                                        <li role="presentation"><a href="#statistics" aria-controls="statistics" role="tab" data-toggle="tab">Statistiques</a></li>
                                        <li role="presentation"><a href="#shops" aria-controls="shops" role="tab" data-toggle="tab">Achats</a></li>
                                    </ul>

                                    <div class="tab-content" style="background-color: white;">
                                        <div role="tabpanel" class="tab-pane active" id="sanctions">
                                            <%
                                                if (UserPermissionLevel.moreThan(((User) request.getSession().getAttribute("user-object")).getPermissionLevel(), UserPermissionLevel.ADMINISTRATOR))
                                                {
                                            %>
                                            <br/>

                                            <div class="text-center">
                                                <button type="button" class="btn btn-danger" data-toggle="modal" data-target="#modalSanction">Ajouter une sanction - Ne pas utiliser (InDev)</button>
                                            </div>

                                            <div class="modal fade" id="modalSanction">
                                                <div class="modal-dialog">
                                                    <div class="modal-content">
                                                        <form class="form-horizontal" action="player" method="post">
                                                            <fieldset>
                                                                <div class="modal-header">
                                                                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                                                                    <h4 class="modal-title">Ajouter une sanction</h4>
                                                                </div>

                                                                <div class="modal-body">
                                                                    <div class="form-group">
                                                                        <label for="inputSanctionType" class="col-lg-2 control-label">Type</label>

                                                                        <div class="col-lg-10">
                                                                            <select class="form-control" name="type" id="inputSanctionType" required>
                                                                                <option value="mute">Mute</option>
                                                                                <option value="ban">Bannissement</option>
                                                                            </select>
                                                                        </div>
                                                                    </div>

                                                                    <div class="form-group">
                                                                        <label for="inputSanctionTime" class="col-lg-2 control-label">Durée</label>

                                                                        <div class="col-lg-10">
                                                                            <input type="number" class="form-control" name="time" id="inputSanctionTime" required>
                                                                        </div>
                                                                    </div>

                                                                    <div class="form-group">
                                                                        <label for="inputSanctionTimeUnit" class="col-lg-2 control-label">Unité de temps</label>

                                                                        <div class="col-lg-10">
                                                                            <select class="form-control" name="unit" id="inputSanctionTimeUnit" required>
                                                                                <option value="m">Minutes</option>
                                                                                <option value="h">Heures</option>
                                                                                <option value="d">Jours</option>
                                                                            </select>
                                                                        </div>
                                                                    </div>

                                                                    <div class="form-group">
                                                                        <label for="inputSanctionReason" class="col-lg-2 control-label">Raison</label>

                                                                        <div class="col-lg-10">
                                                                            <textarea class="form-control" rows="3" name="reason" id="inputSanctionReason" required></textarea>
                                                                        </div>
                                                                    </div>

                                                                    <input type="hidden" name="action" value="sanction" required>
                                                                    <input type="hidden" name="player-name" value="<%= playerName %>" required>
                                                                    <input type="hidden" name="player" value="<%= player.getPlayerUUID().toString() %>" required>
                                                                </div>

                                                                <div class="modal-footer">
                                                                    <button type="submit" class="btn btn-danger">Enregistrer</button>
                                                                </div>
                                                            </fieldset>
                                                        </form>
                                                    </div>
                                                </div>
                                            </div>

                                            <br/>
                                            <%
                                                }
                                            %>

                                            <table class="table table-striped">
                                                <thead>
                                                    <tr>
                                                        <td>Type</td>
                                                        <td>Date</td>
                                                        <td>Appliquée par</td>
                                                        <td>Durée</td>
                                                        <td>Raison</td>
                                                    </tr>
                                                </thead>

                                                <tbody>
                                                    <%
                                                        for (PlayerSanction sanction : player.getSanctions())
                                                        {
                                                            if (sanction.getType() != PlayerSanction.Type.TEXT)
                                                            {
                                                    %>
                                                    <tr>
                                                        <td><%= WordUtils.capitalize(sanction.getType().name().toLowerCase()) %></td>
                                                        <td><%= sanction.getCreation().toString() %></td>
                                                        <td><%= (sanction.getPunisherUUID() != null ? (!sanction.getPunisherUUID().equals(PlayerSanction.SAMARITAN) ? UUIDTranslator.getNameOf(sanction.getPunisherUUID()) : "<span class=\"text-danger\">Samaritan</span>") : "Inconnu") %></td>
                                                        <td><%= sanction.getFormattedTime() %></td>
                                                        <td><%= sanction.getReason() %></td>
                                                    </tr>
                                                    <%
                                                            }
                                                        }
                                                    %>
                                                </tbody>
                                            </table>
                                        </div>

                                        <div role="tabpanel" class="tab-pane" id="comments">
                                            <%
                                                if (UserPermissionLevel.moreThan(((User) request.getSession().getAttribute("user-object")).getPermissionLevel(), UserPermissionLevel.MODERATOR))
                                                {
                                            %>
                                            <br/>

                                            <div class="text-center">
                                                <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#modalCommentary">Ajouter un commentaire</button>
                                            </div>

                                            <div class="modal fade" id="modalCommentary">
                                                <div class="modal-dialog">
                                                    <div class="modal-content">
                                                        <form class="form-horizontal" action="player" method="post">
                                                            <fieldset>
                                                                <div class="modal-header">
                                                                    <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
                                                                    <h4 class="modal-title">Ajouter un commentaire</h4>
                                                                </div>

                                                                <div class="modal-body">
                                                                    <div class="form-group">
                                                                        <label for="inputCommentary" class="col-lg-2 control-label">Texte</label>

                                                                        <div class="col-lg-10">
                                                                            <textarea class="form-control" rows="3" name="commentary" id="inputCommentary" placeholder="Ceci est un magnifique commentaire :)" required></textarea>
                                                                        </div>
                                                                    </div>

                                                                    <input type="hidden" name="action" value="commentary" required>
                                                                    <input type="hidden" name="player-name" value="<%= playerName %>" required>
                                                                    <input type="hidden" name="player" value="<%= player.getPlayerUUID().toString() %>" required>
                                                                </div>

                                                                <div class="modal-footer">
                                                                    <button type="submit" class="btn btn-primary">Enregistrer</button>
                                                                </div>
                                                            </fieldset>
                                                        </form>
                                                    </div>
                                                </div>
                                            </div>

                                            <br/>
                                            <%
                                                }
                                            %>

                                            <table class="table table-striped">
                                                <thead>
                                                    <tr>
                                                        <td>Date</td>
                                                        <td>Par</td>
                                                        <td>Commentaire</td>
                                                    </tr>
                                                </thead>

                                                <tbody>
                                                    <%
                                                        for (PlayerSanction sanction : player.getSanctions())
                                                        {
                                                            if (sanction.getType() == PlayerSanction.Type.TEXT)
                                                            {
                                                    %>
                                                    <tr>
                                                        <td><%= sanction.getCreation().toString() %></td>
                                                        <td><%= UUIDTranslator.getNameOf(sanction.getPunisherUUID()) %></td>
                                                        <td><%= sanction.getReason() %></td>
                                                    </tr>
                                                    <%
                                                            }
                                                        }
                                                    %>
                                                </tbody>
                                            </table>
                                        </div>

                                        <div role="tabpanel" class="tab-pane" id="statistics">
                                            <table class="table">
                                                <thead>
                                                    <tr>
                                                        <td>Jeu</td>
                                                        <td>Nom</td>
                                                        <td>Valeur</td>
                                                    </tr>
                                                </thead>

                                                <tbody>
                                                    <%

                                                        for (String game : statisticsOrdered.keySet())
                                                        {
                                                            boolean first = true;

                                                            HashMap<String, Integer> gameStatistics = statisticsOrdered.get(game);

                                                            for (String gameStatisticKey : gameStatistics.keySet())
                                                            {
                                                    %>
                                                    <tr>
                                                        <%
                                                                if (first)
                                                                {
                                                        %>
                                                        <td rowspan="<%= gameStatistics.size() %>"><%= game %></td>
                                                        <%
                                                                }
                                                        %>

                                                        <td><%= gameStatisticKey %></td>
                                                        <td><%= gameStatistics.get(gameStatisticKey) %></td>
                                                    </tr>
                                                    <%
                                                                first = false;
                                                            }
                                                        }
                                                    %>
                                                </tbody>
                                            </table>
                                        </div>

                                        <div role="tabpanel" class="tab-pane" id="shops">
                                            <table class="table">
                                                <thead>
                                                    <tr>
                                                        <td>Jeu</td>
                                                        <td>Nom</td>
                                                        <td>Achetés</td>
                                                        <td>Equipé</td>
                                                    </tr>
                                                </thead>

                                                <tbody>
                                                    <%

                                                        for (String game : shopsOrdored.keySet())
                                                        {
                                                            boolean first = true;

                                                            HashMap<String, Pair<String[], String>> gameShops = shopsOrdored.get(game);

                                                            for (String gameShopKey : gameShops.keySet())
                                                            {
                                                    %>
                                                    <tr>
                                                        <%
                                                                if (first)
                                                                {
                                                        %>
                                                        <td rowspan="<%= gameShops.size() %>"><%= game %></td>
                                                        <%
                                                                }
                                                        %>

                                                        <td><%= gameShopKey %></td>
                                                        <td><%= StringUtils.join(gameShops.get(gameShopKey).getKey(), ", ") %></td>
                                                        <td><%= gameShops.get(gameShopKey).getValue() %></td>
                                                    </tr>
                                                    <%
                                                                first = false;
                                                            }
                                                        }
                                                    %>
                                                </tbody>
                                            </table>
                                        </div>
                                    </div>
                                </div>
                            </td>
                        </tr>
                    </table>
                </div>
            </div>
        </div>
    </body>
</html>