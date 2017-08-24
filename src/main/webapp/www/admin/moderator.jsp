<%@ page import="net.samagames.samadmin.servlets.admin.charts.ModeratorActivityChart" %>
<%@ page import="net.samagames.samadmin.players.PlayerSanction" %>
<%@ page import="org.apache.commons.lang3.text.WordUtils" %>
<%@ page import="net.samagames.samadmin.utils.UUIDTranslator" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<html>
    <head>
        <%@ include file="../head.jsp" %>

        <script>
            $(function ()
            {
                $("#moderatorActivity").CanvasJSChart(<%= ((ModeratorActivityChart) request.getAttribute("moderator-activity-chart")).build() %>);
            });
        </script>
    </head>

    <body>
        <%@ include file="../sidebar.jsp" %>

        <div class="wrapper">
            <div class="constructor">
                <h2 class="headline">Gestion du modérateur : <%= request.getAttribute("moderator-name") %></h2>

                <br/>

                <div id="moderatorActivity" style="height: 300px; width: 100%; background-color: inherit;"></div>

                <br/>

                <div class="panel panel-primary">
                    <div class="panel-heading">
                        <h3 class="panel-title">Sanctions appliquées (<%= ((ModeratorActivityChart) request.getAttribute("moderator-activity-chart")).getSanctions().size() %>)</h3>
                    </div>

                    <div class="panel-body">
                        <table class="table table-striped">
                            <thead>
                                <tr>
                                    <td>Type</td>
                                    <td>Date</td>
                                    <td>Appliquée sur</td>
                                    <td>Durée</td>
                                    <td>Raison</td>
                                </tr>
                            </thead>

                            <tbody>
                                <%
                                    for (PlayerSanction sanction : ((ModeratorActivityChart) request.getAttribute("moderator-activity-chart")).getSanctions())
                                    {
                                        if (sanction.getType() != PlayerSanction.Type.TEXT)
                                        {
                                %>
                                <tr>
                                    <td><%= WordUtils.capitalize(sanction.getType().name().toLowerCase()) %></td>
                                    <td><%= sanction.getCreation().toString() %></td>
                                    <td><%= UUIDTranslator.getNameOf(sanction.getPlayerUUID()) %></td>
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
                </div>
            </div>
        </div>
    </body>
</html>