<%@ page import="net.samagames.samadmin.servlets.admin.charts.ModeratorsActivityChart" %>
<%@ page import="java.text.DecimalFormat" %>
<%@ page import="java.text.DecimalFormatSymbols" %>
<%@ page import="net.samagames.samadmin.servlets.admin.charts.NewPlayersPerDayChart" %>
<%@ page import="net.samagames.samadmin.servlets.admin.charts.UniquePlayersPerDayChart" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%
    DecimalFormatSymbols decimalFormatSymbols = new DecimalFormatSymbols();
    decimalFormatSymbols.setGroupingSeparator(' ');

    DecimalFormat decimalFormat = new DecimalFormat();
    decimalFormat.setDecimalFormatSymbols(decimalFormatSymbols);
    decimalFormat.setGroupingSize(3);
    decimalFormat.setMaximumFractionDigits(64);
%>
<html>
    <head>
        <%@ include file="../head.jsp" %>

        <script>
            $(function ()
            {
                $("#newPlayersPerDay").CanvasJSChart(<%= ((NewPlayersPerDayChart) request.getAttribute("new-players-per-day-chart")).build() %>);
                $("#uniquePlayersPerDay").CanvasJSChart(<%= ((UniquePlayersPerDayChart) request.getAttribute("unique-players-per-day-chart")).build() %>);
                $("#moderatorsActivity").CanvasJSChart(<%= ((ModeratorsActivityChart) request.getAttribute("moderators-activity-chart")).build() %>);
            });
        </script>
    </head>

    <body>
        <%@ include file="../sidebar.jsp" %>

        <div class="wrapper">
            <div class="constructor">
                <h2 class="headline">Statistiques</h2>

                <br/>

                <p><b>Joueurs uniques : </b><%= decimalFormat.format(request.getAttribute("uniques-players")) %></p><br/>

                <p><b>Total des <span style="color: #f39c12;">Pièces</span> amassées : </b><%= decimalFormat.format(request.getAttribute("total-coins")) %></p>
                <p><b>Total des <span style="color: #4aa3df;">Etoiles</span> amassées : </b><%= decimalFormat.format(request.getAttribute("total-stars")) %></p>

                <br/>

                <div id="newPlayersPerDay" style="height: 300px; width: 100%; background-color: inherit;"></div><br/><br/>
                <div id="uniquePlayersPerDay" style="height: 300px; width: 100%; background-color: inherit;"></div><br/><br/>
                <div id="moderatorsActivity" style="height: 300px; width: 100%; background-color: inherit;"></div><br/><br/>
            </div>
        </div>
    </body>
</html>