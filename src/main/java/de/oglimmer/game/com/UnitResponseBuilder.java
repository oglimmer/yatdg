package de.oglimmer.game.com;

import java.awt.Color;

import org.json.JSONException;
import org.json.JSONObject;

import de.oglimmer.game.logic.Player;
import de.oglimmer.game.logic.board.Field;
import de.oglimmer.game.logic.unit.Unit;

public class UnitResponseBuilder {

	public static JSONObject createField(Field field, Player player)
			throws JSONException {
		JSONObject o = updateField(field, player);
		o.put(ComConst.RO_UI_X, field.getPoint().getX());
		o.put(ComConst.RO_UI_Y, field.getPoint().getY());
		o.put(ComConst.RO_UI_AREA, field.getPoint().getArea());
		o.put(ComConst.RO_UI_COLOR, getColorAsHex(field.getFieldTypeRelation()
				.getColor()));
		o.put(ComConst.RO_UI_FIELDTYPE, field.getFieldTypeRelation()
				.getFieldType().getName());
		return o;
	}

	public static JSONObject updateField(Field field, Player player)
			throws JSONException {
		JSONObject o = new JSONObject();
		o.put(ComConst.RO_UI_FIELDID, field.getId());
		o.put(ComConst.RO_UI_SELECTABLE, field.getPlayerProps(player)
				.isSelectable());
		return o;
	}

	public static JSONObject createUnit(Unit unit, Player player)
			throws JSONException {
		JSONObject o = updateUnit(unit, player);
		o.put(ComConst.RO_UI_COLOR, unit.getOwningPlayer().getUnitColor());
		o.put(ComConst.RO_UI_NAME, unit.getName());
		o.put(ComConst.RO_UI_MAXLIFE, unit.getMaxLife());
		o.put(ComConst.RO_UI_TEXT, unit.getText());
		return o;
	}

	public static JSONObject updateUnit(Unit unit, Player player)
			throws JSONException {
		JSONObject o = new JSONObject();
		o.put(ComConst.RO_UI_UNITID, unit.getId());
		o.put(ComConst.RO_UI_X, unit.getPoint(player).getX());
		o.put(ComConst.RO_UI_Y, unit.getPoint(player).getY());
		o.put(ComConst.RO_UI_AREA, unit.getPoint(player).getArea());
		o.put(ComConst.RO_UI_LIFE, unit.getLife());
		o.put(ComConst.RO_UI_SELECTABLE, unit.getPlayerProps(player)
				.isSelectable());
		return o;
	}

	private static String getColorAsHex(Color color) {
		String red = Integer.toHexString(color.getRed());
		if (red.length() < 2) {
			red = "0" + red;
		}
		String green = Integer.toHexString(color.getGreen());
		if (green.length() < 2) {
			green = "0" + green;
		}
		String blue = Integer.toHexString(color.getBlue());
		if (blue.length() < 2) {
			blue = "0" + blue;
		}
		return "#" + red + green + blue;
	}
}
