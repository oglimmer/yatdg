package de.oglimmer.game.com;

public interface ComConst {

	// used as main attributes in the JSON response
	String RO_UPDATEUNITS = "updateUnits";
	String RO_UPDATEFIELDS = "updateFields";
	String RO_NEWUNITS = "newUnits";
	String RO_NEWFIELDS = "newFields";
	String RO_SHOW_WAIT_DIALOG = "showWaitDialog";
	String RO_WAIT_DIALOG_TEXT = "waitDialogText";
	String RO_PLAYERID = "playerId";
	String RO_ENABLEENDTURN = "enableEndTurn";
	String RO_GAMEID = "gameId";
	String RO_ACTIONID = "actionId";
	String RO_UIELEMENTID = "uiElementId";
	String RO_PARAMETERS = "parameters";
	String RO_ENDGAME = "endGame";
	String RO_ENDGAMETEXT = "endGameText";
	String RO_SHOWBATTLE = "showBattle";
	String RO_ATTACKER = "attacker";
	String RO_TARGET = "target";
	String RO_DAMAGE = "damage";
	String RO_BATTLEINFOCOLOR = "battleInfoColor";
	String RO_HELPTEXT = "helpText";
	String RO_ERROR = "error";

	// used as action ids
	String ACTION_INIT = "init";
	String ACTION_SELECT = "select";
	String ACTION_REINIT = "reinit";
	String ACTION_END_TURN = "endturn";
	String ACTION_NO_OP = "noop";

	// used as sub attributes in new/update/fields/units
	String RO_UI_X = "x";
	String RO_UI_Y = "y";
	String RO_UI_FIELDTYPE = "fieldType";
	String RO_UI_COLOR = "color";
	String RO_UI_AREA = "area";
	String RO_UI_UNITID = "unitId";
	String RO_UI_LIFE = "life";
	String RO_UI_SELECTABLE = "selectable";
	String RO_UI_FIELDID = "fieldId";
	String RO_UI_MAXLIFE = "maxLife";
	String RO_UI_NAME = "name";
	String RO_UI_TEXT = "text";

	String MSG_DMG_ATTACKE = "Hit damaged an enemy unit";
	String MSG_DMG_TARGET = "Your unit got damage";
	String MSG_MOVEMENT = "Movement in progress. Wait and watch...";
	String MSG_NEW_TURN = "A new turn. If you like select an unit to deploy it.";
	String MSG_WAIT = "Wait until your opponent finished the turn.";
	String MSG_INITIAL = "Capture the black castle within 5 rounds! First select one or none of the ranged units. Ranged units are Archer, Wizard or Ram. To select one click on it on the right side of the board.";
	String MSG_AFTER_SELECT = "Place a unit on the end of your roads. Possible deploy fields are marked. To de-select a unit click on it again.";
	String MSG_AFTER_DEPLOY = "Deploy your units. To select one click on it on the right side of the board.";

}
