			
/* 低い解像度のマップを使うようにする */
window.devicePixelRatio = 1;

var _im = {
	map: null,
	scrollToCurrentLocation: function() {
		var self = this;
		if (navigator.geolocation) {
			var options = {
				enableHighAccuracy:true,
				timeout:10000,
				maximumAge:10000
			};
			navigator.geolocation.getCurrentPosition(
				function (position) {
				    var loc = new google.maps.LatLng(
				    	position.coords.latitude,
				    	position.coords.longitude);
				    self.map.setCenter(loc);
				}, 
				function() {}, options);
		}
	},
	zoom: function(z) {
		document.body.style.zoom = z;
	},
};

(function() {
	var init_ui = function () {
		/* 現在地ボタンを隠す */
		var sc = document.getElementById('snapcontrol');
		var dc = document.getElementById('dashboard_container');
		var map = nemesis.dashboard.Dashboard.getInstance().map_;
		if (sc != undefined && dc != undefined
				&& map != undefined) {
			_im.map = map;
			sc.style.display = 'none';
			dc.style.border = '0px';
			
			var marker = new google.maps.Marker({
				icon: { path: google.maps.SymbolPath.CIRCLE, scale:10, strokeWeight:4, strokeColor:"#e00" },
			});

			var styles = [
			  {
			    "stylers": [
			      { "invert_lightness": true },
			      { "saturation": -21 }
			    ]
			  },{
			    "featureType": "road",
			    "stylers": [
			      { "visibility": "simplified" },
			      { "saturation": -56 },
			      { "lightness": -29 }
			    ]
			  },{
			    "featureType": "poi",
			    "stylers": [
			      { "visibility": "simplified" }
			    ]
			  },{
			    "featureType": "poi",
			    "elementType": "labels",
			    "stylers": [
			      { "visibility": "off" }
			    ]
			  }
			];
			_im.map.setOptions({styles:styles});


			var watchid = navigator.geolocation.watchPosition(
				function (position) {
					console.log("position:" + position);
				    var loc = new google.maps.LatLng(
				    	position.coords.latitude,
				    	position.coords.longitude);
				    	
					marker.setPosition(loc);
					marker.setMap(_im.map);
				},
				null,
				{'enableHighAccuracy':true,'timeout':10000,'maximumAge':5000}
			);
			
		} else {
			setTimeout(init_ui, 500);
		}
	};
	init_ui();
	
	
})();
