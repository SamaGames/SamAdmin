package net.samagames.samadmin.common;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.LinkedHashMap;
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
public class Chart
{
    private final String title;
    private final HashMap<UUID, ChartData> entries;

    public Chart(String title)
    {
        this.title = title;
        this.entries = new HashMap<>();
    }

    public String build()
    {
        JsonObject rootJson = new JsonObject();

        JsonObject titleJson = new JsonObject();
        titleJson.addProperty("text", this.title);
        rootJson.add("title", titleJson);

        rootJson.addProperty("animationEnabled", true);

        JsonObject toolTipJson = new JsonObject();
        toolTipJson.addProperty("shared", "true");
        rootJson.add("toolTip", toolTipJson);

        JsonArray dataJson = new JsonArray();

        for (ChartData entry : this.entries.values())
        {
            JsonObject entryJson = new JsonObject();

            entryJson.addProperty("type", entry.getType());
            entryJson.addProperty("name", entry.getName());
            entryJson.addProperty("showInLegend", entry.showInLegend());

            if (!entry.showMarker())
                entryJson.addProperty("markerSize", 0);

            JsonArray dataPointsJson = new JsonArray();

            for (String dataPointLabel : entry.getValues().keySet())
            {
                JsonObject dataPointJson = new JsonObject();
                dataPointJson.addProperty("label", dataPointLabel);
                dataPointJson.addProperty("y", entry.getValues().get(dataPointLabel));

                dataPointsJson.add(dataPointJson);
            }

            entryJson.add("dataPoints", dataPointsJson);

            dataJson.add(entryJson);
        }

        rootJson.add("data", dataJson);

        return new Gson().toJson(rootJson);
    }

    public ChartData addEntry(String type, String name, boolean showMarker, boolean showInLegend)
    {
        UUID randomUUID = UUID.randomUUID();
        ChartData entry = new ChartData(randomUUID, type, name, showMarker, showInLegend);

        this.entries.put(randomUUID, entry);
        return entry;
    }

    public void addPoint(UUID entryUUID, String label, int value)
    {
        if (this.entries.containsKey(entryUUID))
            this.entries.get(entryUUID).addValue(label, value);
    }

    public static class ChartData
    {
        private final UUID uuid;
        private final String type;
        private final String name;
        private final LinkedHashMap<String, Integer> values;
        private final boolean showMarker;
        private final boolean showInLegend;

        public ChartData(UUID uuid, String type, String name, boolean showMarker, boolean showInLegend)
        {
            this.uuid = uuid;
            this.type = type;
            this.name = name;
            this.showMarker = showMarker;
            this.showInLegend = showInLegend;

            this.values = new LinkedHashMap<>();
        }

        public void addValue(String label, int value)
        {
            this.values.put(label, value);
        }

        public UUID getUUID()
        {
            return this.uuid;
        }

        public String getType()
        {
            return this.type;
        }

        public String getName()
        {
            return this.name;
        }

        public boolean showMarker()
        {
            return this.showMarker;
        }

        public boolean showInLegend()
        {
            return this.showInLegend;
        }

        public HashMap<String, Integer> getValues()
        {
            return this.values;
        }
    }
}
