// CONSTS
var size = {
	width : 40,
	height : 48
};
var baseColor = "red";
var selectedColor = "black";
var debug = false;

/**
 * CLASS Board
 * 
 * @parameter fields an array of Field objects
 */
function Board() {
	this.idToFields = {}; // map<id,field>
	this.idToUnits = {};
	this.corToFields = {}; // map<xCor:yCor, field>
	this.currentActiveCor = {
		x : -1,
		y : -1
	};
	this.attacker = null;
	this.target = null;
	this.damage = 0;
	this.battleInfoColor = null;
}

Board.prototype.setFields = function(fields) {
	for ( var i = 0; i < fields.length; i++) {
		this.idToFields[fields[i].id] = fields[i];
		this.corToFields[fields[i].x + ":" + fields[i].y] = fields[i];
	}
};

Board.prototype.setUnits = function(units) {
	for ( var i = 0; i < units.length; i++) {
		this.idToUnits[units[i].id] = units[i];
	}
};

Board.prototype.setFightUnits = function(attacker, target, damage,
		battleInfoColor) {
	if (attacker == -1) {
		this.attacker = null;
		this.target = null;
		this.damage = 0;
	} else {
		this.attacker = this.idToUnits[attacker];
		this.target = this.idToUnits[target];
		this.damage = damage;
		this.battleInfoColor = battleInfoColor;
	}
};

Board.prototype.searchOtherUnitOnSameLocation = function(unitToSearch) {
	for ( var f in this.idToUnits) {
		var unit = this.idToUnits[f];
		if (unit != unitToSearch && unit.x == unitToSearch.x
				&& unit.y == unitToSearch.y) {
			return unit;
		}
	}
	return null;
};

/**
 * draw the board to the context ctx
 */
Board.prototype.draw = function(ctx) {
	ctx.clearRect(0, 0, ctx.canvas.width, ctx.canvas.height);

	// ctx.fillStyle = "#f1f1f1";
	// ctx.fillRect(0, 0, ctx.canvas.width, ctx.canvas.height);

	var fc = 0, uc = 0;
	for ( var f in this.idToFields) {
		this.idToFields[f].draw(ctx);
		fc++;
	}
	for ( var f in this.idToUnits) {
		var unitToDraw = this.idToUnits[f];
		var otherUnitOnSameLocation = this
				.searchOtherUnitOnSameLocation(unitToDraw);
		if (otherUnitOnSameLocation == null
				|| otherUnitOnSameLocation.life >= unitToDraw.life) {
			unitToDraw.draw(ctx);
		}
		uc++;
	}

	if (this.attacker != null) {
		var ax = this.attacker.middleX();
		var ay = this.attacker.middleY();
		var tx = this.target.middleX();
		var ty = this.target.middleY();

		ctx.beginPath();
		ctx.moveTo(ax, ay);
		ctx.lineTo(tx, ty);
		ctx.lineWidth = 4;
		ctx.stroke();

		ctx.beginPath();
		ctx.arc((ax - tx) / 2 + tx, (ay - ty) / 2 + ty, 20, 0, 2 * Math.PI,
				false);
		ctx.fillStyle = "#8ED6FF";
		ctx.fill();
		ctx.lineWidth = 5;
		ctx.strokeStyle = "black";
		ctx.stroke();

		ctx.font = "20pt Calibri";
		ctx.strokeStyle = this.battleInfoColor;
		var metrics = ctx.measureText(this.damage);
		ctx.strokeText(this.damage, (ax - tx) / 2 + tx - metrics.width / 2,
				(ay - ty) / 2 + ty + 10);
	}
	// console.log("fields:" + fc + ", unit:" + uc);
};

/**
 * returns a Field object which is located at pos (x,y)
 * http://www.uni-protokolle.de/foren/viewt/92618,0.html
 */
Board.prototype.convertMousePosToHex = function(pos) {
	// x,y base coordinate
	var hexPosY = Math.floor(pos.y / (size.height * 0.75));
	var hexPosX = Math.floor(pos.x / size.width);
	// odd or even row?
	var rowSelector = hexPosY % 2;

	// x,y rel to current element
	var posYRel = pos.y - hexPosY * size.height * 0.75;
	var posXRel = pos.x - hexPosX * size.width;

	switch (rowSelector) {
	case 0:
		// odd row
		if (posYRel < size.height / 4) {
			// the top part (looks like a roof ^ )
			var s = posXRel / (size.width / 2);
			var r = posYRel / (size.height / 4);
			if (0 <= r && s <= 1 && r + s <= 1) {
				// left of the roof (belongs to previous row and column)
				hexPosX--;
				hexPosY--;
			} else {
				s = posYRel / (size.height / 4);
				r = posXRel / (size.width / 2) - s - 1;
				if (0 <= r && s <= 1 && r + s <= 1) {
					// right of the roof (belongs to previous row)
					hexPosY--;
				}
				// no change for "under the roof"
			}
		}
		break;
	case 1:
		// even row
		if (posYRel > size.height / 4) {
			// lower part (divided in the middle)
			if (posXRel < size.width / 2) {
				// left part (belongs to previous hex)
				hexPosX--;
			}
		} else {
			// upper part (looks like a "v")
			var s = posXRel / (size.width / 2);
			var r = posYRel / (size.height / 4) - s;
			if (0 <= r && s <= 1 && r + s <= 1) {
				// left lower part of the v
				hexPosX--;
			} else {
				var s = (posXRel - size.width) / (size.width / 2) * -1;
				var r = (posYRel / (size.height / 4)) - s;
				if (!(0 <= r && s <= 1 && r + s <= 1)) {
					// middle part v (or not the right lower part)
					hexPosY--;
				}
			}
		}
		break;
	}

	if (this.corToFields.hasOwnProperty(hexPosX + ":" + hexPosY)) {
		return this.corToFields[hexPosX + ":" + hexPosY];
	}
	return null;
};

/*
 * Selects the Field field
 */
Board.prototype.select = function(field, ctx) {
	if (field == null) {
		return;
	}
	var selX = field.x;
	var selY = field.y;
	if (selX >= 0 && selY >= 0) {
		if (selX != this.currentActiveCor.x || selY != this.currentActiveCor.y) {
			if (this.currentActiveCor.x >= 0 && this.currentActiveCor.y >= 0) {
				this.corToFields[this.currentActiveCor.x + ":"
						+ this.currentActiveCor.y].color = baseColor;
			}
			this.corToFields[selX + ":" + selY].color = selectedColor;
			this.draw(ctx);
			this.currentActiveCor.x = selX;
			this.currentActiveCor.y = selY;
		}
	}
};

/**
 * CLASS Unit
 * 
 * @parameter id unique id
 * @parameter color to use
 * @parameter pos on x,y objects
 */
function Unit(id, color, pos, name, life, maxLife, selectable, text) {
	this.id = id;
	this.color = color;
	this.x = pos[0];
	this.y = pos[1];
	this.width = size.width;
	this.height = size.height;
	this.name = name;
	this.life = life;
	this.maxLife = maxLife;
	this.selectable = selectable;
	this.text = text;
	this.mx = 0;
	this.my = 0;
}

Unit.prototype.middleX = function(ctx) {
	var x = this.x;
	var y = this.y;
	var width = this.width, cx = width + x * width - (y + 1) % 2 * width / 2;
	return cx;
};

Unit.prototype.middleY = function(ctx) {
	var y = this.y;
	var height = this.height, cy = 0.5 * height + y * (3 / 4 * height);
	return cy;
};

/*
 * draws the field
 */
Unit.prototype.draw = function(ctx) {
	var x = this.x;
	var y = this.y;
	var width = this.width;
	var height = this.height;
	var cx = width + x * width - (y + 1) % 2 * width / 2 + this.mx;
	var cy = 0.5 * height + y * (3 / 4 * height) + this.my;

	ctx.beginPath();
	ctx.moveTo(cx, cy - height / 2);
	ctx.lineTo(cx + width / 2, cy - height / 4);
	ctx.lineTo(cx + width / 2, cy + height / 4);
	ctx.lineTo(cx, cy + height / 2);
	ctx.lineTo(cx - width / 2, cy + height / 4);
	ctx.lineTo(cx - width / 2, cy - height / 4);
	ctx.lineTo(cx, cy - height / 2);
	ctx.fillStyle = this.color;
	ctx.fill();

	ctx.strokeStyle = "blue";
	ctx.lineWidth = this.selectable ? 3 : 1;
	ctx.stroke();

	ctx.fillStyle = '#00f';
	ctx.font = '8px sans-serif';
	ctx.fillText(this.name, cx - 19, cy - 4);

	ctx.fillStyle = "white";
	ctx.fillRect(cx - 16, cy, 30, 4);
	ctx.fillStyle = "red";
	ctx.fillRect(cx - 16, cy, 30 * (this.life / this.maxLife), 4);
	ctx.strokeStyle = 'black'; // red
	ctx.lineWidth = 1;
	ctx.strokeRect(cx - 16, cy, 30, 4);

};

/**
 * CLASS Field
 * 
 * @parameter id unique id
 * @parameter color to use
 * @parameter pos on x,y objects
 */
function Field(id, color, pos, selectable, fieldType) {
	this.id = id;
	this.color = color;
	this.x = pos[0];
	this.y = pos[1];
	this.width = size.width;
	this.height = size.height;
	this.selectable = selectable;
	this.fieldType = fieldType;
	// console.log("New field "+this.x+"/"+this.y+" , "+this.color);
}

/*
 * draws the field
 */
Field.prototype.draw = function(ctx) {
	var x = this.x;
	var y = this.y;
	var width = this.width, height = this.height, cx = width + x * width
			- (y + 1) % 2 * width / 2, cy = 0.5 * height + y * (3 / 4 * height);

	ctx.beginPath();
	ctx.moveTo(cx, cy - height / 2);
	ctx.lineTo(cx + width / 2, cy - height / 4);
	ctx.lineTo(cx + width / 2, cy + height / 4);
	ctx.lineTo(cx, cy + height / 2);
	ctx.lineTo(cx - width / 2, cy + height / 4);
	ctx.lineTo(cx - width / 2, cy - height / 4);
	ctx.lineTo(cx, cy - height / 2);
	ctx.fillStyle = this.color;
	ctx.fill();

	if (this.selectable) {
		ctx.strokeStyle = "blue";
		ctx.lineWidth = 3;
		ctx.stroke();
	}

};

// MAIN

var canvasHand = document.getElementById('hand');
var ctxHand = canvasHand.getContext('2d');

var canvasBoard = document.getElementById('board');
var ctxBoard = canvasBoard.getContext('2d');

var canvasInfo = document.getElementById('info');
var ctxInfo = canvasInfo.getContext('2d');

var globalFieldCounter = 0;
var playerId = -1;
var board = new Board();
var hand = new Board();
var sv = new ServerCommunication();
var gameStarted = false;

// INIT LISTENER
window.onload = function() {
	canvasBoard.addEventListener('mousemove', function(evt) {
		var mousePos = getRelMousePos(canvasBoard, evt);
		var selectedHex = board.convertMousePosToHex(mousePos);
		if (selectedHex != null) {
			// board.select(selectedHex, ctxBoard);
			drawInfo(ctxInfo, selectedHex, board);
		}
	}, false);

	canvasHand.addEventListener('mousemove', function(evt) {
		var mousePos = getRelMousePos(canvasHand, evt);
		var selectedHex = hand.convertMousePosToHex(mousePos);
		if (selectedHex != null) {
			drawInfo(ctxInfo, selectedHex, hand);
		}
	}, false);

	canvasBoard.addEventListener('click', function(evt) {
		console.log("click");
		var mousePos = getRelMousePos(canvasBoard, evt);
		var selectedHex = board.convertMousePosToHex(mousePos);
		if (selectedHex != null) {

			if (selectedHex.selectable) {
				sv.subSocket.push(JSON.stringify({
					gameId : gameId,
					playerId : playerId,
					actionId : "select",
					uiElementId : selectedHex.id
				}));
			}

			for ( var unitProp in board.idToUnits) {
				var unit = board.idToUnits[unitProp];
				if (unit.selectable && unit.x == selectedHex.x
						&& unit.y == selectedHex.y) {
					sv.subSocket.push(JSON.stringify({
						gameId : gameId,
						playerId : playerId,
						actionId : "select",
						uiElementId : unit.id
					}));
				}
			}

		}
	}, false);

	canvasHand.addEventListener('click', function(evt) {
		console.log("click");
		var mousePos = getRelMousePos(canvasHand, evt);
		var selectedHex = hand.convertMousePosToHex(mousePos);
		if (selectedHex != null) {

			for ( var unitProp in hand.idToUnits) {
				var unit = hand.idToUnits[unitProp];
				if (unit.selectable && unit.x == selectedHex.x
						&& unit.y == selectedHex.y) {
					sv.subSocket.push(JSON.stringify({
						gameId : gameId,
						playerId : playerId,
						actionId : "select",
						uiElementId : unit.id
					}));
				}
			}

		}
	}, false);

};

function drawInfo(ctx, selectedHex, board) {
	ctx.fillStyle = '#00f';
	ctx.fillRect(0, 0, 400, 400);
	ctx.font = "12pt Calibri";
	ctx.fillStyle = '#f00';

	for ( var unitProp in board.idToUnits) {
		var unit = board.idToUnits[unitProp];
		if (unit.x == selectedHex.x && unit.y == selectedHex.y) {
			ctx.fillText("Name:" + unit.name, 5, 15);
			ctx.fillText("Life:" + unit.life + "/" + unit.maxLife, 5, 35);
			wrapText(ctx, unit.text, 5, 55, 200, 20);
		}
	}

}

function wrapText(context, text, x, y, maxWidth, lineHeight) {
	var words = text.split(" ");
	var line = "";

	for ( var n = 0; n < words.length; n++) {
		var testLine = line + words[n] + " ";
		var metrics = context.measureText(testLine);
		var testWidth = metrics.width;
		if (testWidth > maxWidth) {
			context.fillText(line, x, y);
			line = words[n] + " ";
			y += lineHeight;
		} else {
			line = testLine;
		}
	}
	context.fillText(line, x, y);
}

function getRelMousePos(canvas, evt) {
	var obj = canvas;
	var top = 0;
	var left = 0;
	while (obj && obj.tagName != 'BODY') {
		top += obj.offsetTop;
		left += obj.offsetLeft;
		obj = obj.offsetParent;
	}

	var mouseX = evt.clientX - left + window.pageXOffset;
	var mouseY = evt.clientY - top + window.pageYOffset;
	return {
		x : mouseX,
		y : mouseY
	};
}

function endTurn() {
	sv.subSocket.push(JSON.stringify({
		gameId : gameId,
		playerId : playerId,
		actionId : "endturn"
	}));
}

var fifo = [];

/**
 * 
 */
function ServerCommunication(url) {

	var socket = window.atmosphere;
	var request = {
		url :'http://'+location.host+'/yatdg/srvcom',
		contentType : "application/json",
		logLevel : 'debug',
		transport : 'websocket',
		fallbackTransport : 'long-polling'
	};
	request.onOpen = function(response) {
		console.log("Con opened. " + getCookie("playerId"));
		if (!debug || getCookie("playerId") == "") {
			subSocket.push(JSON.stringify({
				gameId : gameId,
				actionId : "init"
			}));
		} else {
			subSocket.push(JSON.stringify({
				gameId : gameId,
				playerId : getCookie("playerId"),
				actionId : "reinit"
			}));
			playerId = getCookie("playerId");
		}
	};
	request.onMessage = function(response) {
		console.log("onMessage");
		var message = response.responseBody;
		try {
			var ret = JSON.parse(message);
			console.log(ret);
			fifo.push(ret);
			console.log("added item to fifo :" + ret);
			if (!running) {
				running = true;
				processFifo();
			}
		} catch (e) {			
			console.log('This doesn`t look like a valid JSON: ',message);
		}
	};

	request.onError = function(response) {
		console.log("Con failed! " + response.data);
	};
	
	request.onClose = function(evt) {
		console.log("Con closed");
	};

	var subSocket = socket.subscribe(request);
	this.subSocket = subSocket;
};

var running = false;

function processFifo() {

	var cont = false;
	do {
		if (fifo.length > 0) {
			var ret = fifo.shift();
			processFifoElmenet(ret);
			if (ret.hasOwnProperty("waitTime")) {
				setTimeout("processFifo()", ret.waitTime);
				cont = false;
			} else if (ret.hasOwnProperty("dynWaitTime")) {
				cont = false;
			} else {
				cont = fifo.length > 0;
				if (!cont) {
					running = false;
				}
			}
		} else {
			running = false;
		}
	} while (cont);
}

function getx(x, y) {
	var cx = size.width + x * size.width - (y + 1) % 2 * size.width / 2;
	return cx;
};
function gety(y) {
	var cy = 0.5 * size.height + y * (3 / 4 * size.height);
	return cy;
};

var movingUnit = null;

function moveUnit() {
	var moving = false;
	for ( var i = 0; i < movingUnit.length; i++) {
		var f = movingUnit[i];
		var exitingUnit;
		if (board.idToUnits.hasOwnProperty(f.unitId)) {
			exitingUnit = board.idToUnits[f.unitId];
		} else {
			exitingUnit = hand.idToUnits[f.unitId];
		}
		if (exitingUnit.mx != 0) {
			if (exitingUnit.mx < 0) {
				exitingUnit.mx++;
			} else {
				exitingUnit.mx--;
			}
		}
		if (exitingUnit.my != 0) {
			if (exitingUnit.my < 0) {
				exitingUnit.my++;
			} else {
				exitingUnit.my--;
			}
		}

		if (exitingUnit.mx != 0 || exitingUnit.my != 0) {
			moving = true;
		}
	}
	board.draw(ctxBoard);
	if (moving) {
		setTimeout("moveUnit()", 10);
	} else {
		processFifo();
	}
}

function processFifoElmenet(ret) {

	if (ret.hasOwnProperty("error")) {
		window.atmosphere.unsubscribe();
		console.log("Got error : " + ret.error);
		document.getElementById("overlayText").innerHTML = "FATAL ERROR<br/>"
				+ ret.error + "<br/><a href='index.jsp'>Start over</a>";
		overlay(true);
	}

	if (ret.hasOwnProperty("newFields")) {
		var fields = [];
		var newFields = ret.newFields;
		for ( var i = 0; i < newFields.length; i++) {
			var f = newFields[i];
			// we don't evaluate the area parameter since it is always board
			fields.push(new Field(f.fieldId, f.color, [ f.x, f.y ],
					f.selectable, f.fieldType));
		}
		board.setFields(fields);
	}
	if (ret.hasOwnProperty("updateFields")) {
		var updateFields = ret.updateFields;
		for ( var i = 0; i < updateFields.length; i++) {
			var f = updateFields[i];
			board.idToFields[f.fieldId].selectable = f.selectable;
		}
	}

	if (ret.hasOwnProperty("newUnits")) {
		var unitsBoard = [];
		var unitsHand = [];
		var newUnits = ret.newUnits;
		for ( var i = 0; i < newUnits.length; i++) {
			var f = newUnits[i];
			var newUnit = new Unit(f.unitId, f.color, [ f.x, f.y ], f.name,
					f.life, f.maxLife, f.selectable, f.text);
			if (f.area == 'BOARD') {
				unitsBoard.push(newUnit);
			} else if (f.area == 'HAND') {
				unitsHand.push(newUnit);
				var baseField = [ new Field(globalFieldCounter++, "white", [
						f.x, f.y ], false, "Hand") ];
				hand.setFields(baseField);
			}
		}
		board.setUnits(unitsBoard);
		hand.setUnits(unitsHand);
	}

	if (ret.hasOwnProperty("updateUnits")) {
		var updateUnits = ret.updateUnits;
		var moving = false;
		var changedArea = false;
		for ( var i = 0; i < updateUnits.length; i++) {
			var f = updateUnits[i];
			var exitingUnit;
			if (board.idToUnits.hasOwnProperty(f.unitId)) {
				exitingUnit = board.idToUnits[f.unitId];
				if (f.area == 'GRAVEYARD') {
					delete board.idToUnits[f.unitId];
					changedArea = true;
				}
			} else {
				exitingUnit = hand.idToUnits[f.unitId];
				if (f.area == 'BOARD') {
					delete hand.idToUnits[f.unitId];
					board.idToUnits[f.unitId] = exitingUnit;
					changedArea = true;
				}
			}
			if (!changedArea && f.area == 'BOARD') {
				exitingUnit.mx = (getx(f.x, f.y) - getx(exitingUnit.x,
						exitingUnit.y))
						* -1;
				exitingUnit.my = (gety(f.y) - gety(exitingUnit.y)) * -1;
				if (exitingUnit.mx != 0 || exitingUnit.my != 0) {
					moving = true;
				}
			}

			exitingUnit.x = f.x;
			exitingUnit.y = f.y;
			exitingUnit.life = f.life;
			exitingUnit.selectable = f.selectable;

		}
		if (moving) {
			movingUnit = updateUnits;
			ret.dynWaitTime = true;
			setTimeout("moveUnit()", 10);
		}
	}

	if (ret.hasOwnProperty("playerId")) {
		playerId = ret.playerId;
		setCookie("playerId", playerId);
	}

	if (ret.hasOwnProperty("showWaitDialog")) {
		console.log(document.getElementById("overlayText").innerHTML);
		document.getElementById("overlayText").innerHTML = "Waiting for other players";
		overlay(ret.showWaitDialog);
		if (ret.showWaitDialog) {
			gameStarted = false;
		} else {
			gameStarted = true;
		}
	}

	if (ret.hasOwnProperty("endGame")) {
		document.getElementById("overlayText").innerHTML = ret.endGameText;
		overlay(true);
	}

	if (ret.hasOwnProperty("enableEndTurn")) {
		var enableEndTurn = ret.enableEndTurn;
		document.getElementById("endTurnButton").disabled = !enableEndTurn;
	}

	if (ret.hasOwnProperty("showBattle")) {
		if (ret.showBattle) {
			var attacker = ret.attacker;
			var target = ret.target;
			board.setFightUnits(attacker, target, ret.damage,
					ret.battleInfoColor);
		} else {
			board.setFightUnits(-1, -1);
		}
	}

	if (ret.hasOwnProperty("helpText")) {
		document.getElementById("helpText").innerHTML = ret.helpText;
	}

	board.draw(ctxBoard);
	hand.draw(ctxHand);
}

// modal overlay

function overlay(show) {
	var el = document.getElementById("overlay");
	if (show) {
		el.style.visibility = "visible";
	} else {
		el.style.visibility = "hidden";
	}
}

// cookie stuff

function setCookie(cookieName, cookieValue, nDays) {
	var today = new Date();
	var expire = new Date();
	if (nDays == null || nDays == 0) {
		nDays = 1;
	}
	expire.setTime(today.getTime() + 3600000 * 24 * nDays);
	document.cookie = cookieName + "=" + escape(cookieValue) + ";expires="
			+ expire.toGMTString();
};

function getCookie(cookieName) {
	var theCookie = " " + document.cookie;
	var ind = theCookie.indexOf(" " + cookieName + "=");
	if (ind == -1) {
		ind = theCookie.indexOf(";" + cookieName + "=");
	}
	if (ind == -1 || cookieName == "") {
		return "";
	}
	var ind1 = theCookie.indexOf(";", ind + 1);
	if (ind1 == -1) {
		ind1 = theCookie.length;
	}
	return unescape(theCookie.substring(ind + cookieName.length + 2, ind1));
};
