<?php
include 'Classes.php';

function formatSizeUnits($bytes)
{
    if ($bytes >= 1073741824) {
        $bytes = number_format($bytes / 1073741824, 2) . ' GB';
    } elseif ($bytes >= 1048576) {
        $bytes = number_format($bytes / 1048576, 2) . ' MB';
    } elseif ($bytes >= 1024) {
        $bytes = number_format($bytes / 1024, 2) . ' KB';
    } elseif ($bytes > 1) {
        $bytes = $bytes . ' bytes';
    } elseif ($bytes == 1) {
        $bytes = $bytes . ' byte';
    } else {
        $bytes = '0 bytes';
    }
    
    return $bytes;
}

function rcommandTitleSection($title){
    $str = <<<EOD
	<div class="rcon-container-title">
		<h1>$title</h1>
	</div>
EOD;
    return $str;
}

?>


<!DOCTYPE HTML>
<html lang="en" prefix="og: http://ogp.me/ns#">

<head>

	<meta charset="UTF-8">
	<title>IXI-GO: Monday Nights</title>
	<!-- Meta Information -->
	<meta name="theme-color" content="#d0d0d0">
	<meta name="msapplication-navbutton-color" content="#d0d0d0">
	<meta name="apple-mobile-web-app-status-bar-style" content="#d0d0d0">
	<meta name="apple-mobile-web-app-capable" content="yes" />
	<meta name="mobile-web-app-capable" content="yes" />
	<link rel="icon" sizes="196x196" href="http://marco.selfip.net/cstrike/pictures/ixigo-logo.png">
	<link rel="apple-touch-icon" href="http://marco.selfip.net/cstrike/pictures/ixigo-logo.png">
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name="viewport" id="vp" content="initial-scale=1.0,user-scalable=no,maximum-scale=1" />
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
	<meta name="description" content="IXI-GO : Every Monday evening we play Counter Strike Global Offencive (CSGO) on our dedicated server. Here we share the game results and some info" />
	<meta property="og:title" content="IXI-GO: Monday Nights" />
	<meta property="og:description" content="IXI-GO : Every Monday evening we play Counter Strike Global Offencive (CSGO) on our dedicated server. Here we share the game results and some info" />
	<meta property="og:type" content="website" />
	<meta property="og:url" content="http://marco.selfip.net/cstrike" />
	<meta property="og:image" content="http://marco.selfip.net/cstrike/pictures/ixigo-logo.jpg" />
	<meta http-equiv="Content-Security-Policy" content="upgrade-insecure-requests"> 

	<!-- CSS -->
	<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@4.6.0/dist/css/bootstrap.min.css" integrity="sha384-B0vP5xmATw1+K9KRQjQERJvTumQW0nPEzvF6L/Z6nronJ3oUOFUFpCjEUQouq2+l" crossorigin="anonymous">
	<link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
	<link rel="stylesheet" href="./css/style.css">
	<link rel="stylesheet" href="./css/animate.css">
	<link rel="stylesheet" href="./css/pnotify.custom.min.css">
	

	<!-- Javascript -->
	<script src="https://code.jquery.com/jquery-3.5.1.min.js" integrity="sha256-9/aliU8dGd2tb6OSsuzixeV4y/faTqgFtohetphbbj0=" crossorigin="anonymous"></script>
	<script src="https://cdn.jsdelivr.net/npm/bootstrap@4.6.0/dist/js/bootstrap.bundle.min.js" integrity="sha384-Piv4xVNRyMGpqkS2by6br4gNJ7DXjqk09RmUpJ8jgGtD7zP9yug3goQfGII0yAns" crossorigin="anonymous"></script>
	<script src="./js/Chart.min.js"></script>
	<script src="./js/pnotify.custom.min.js"></script>
	<script src="./js/MarcoUtils.js"></script>
	<script src="./js/PlayersManager.js"></script>
	<script src="./js/Csgo.js"></script>
	<script src="./js/CsgoChart.js"></script>
	<script src="./js/CsgoChartDataset.js"></script>
	<script src="./js/PlayerScoreChart.js"></script>
	<script src="./js/MapsPlayedChart.js"></script>
	<script src="./js/ChartsManager.js"></script>
	<script src="./js/DiscordBot.js"></script>

	<script>
		<?php
		$demFilesRootFolder = "./demfiles";
		$filesRootFolder = "./rcon/maps";
		$apiBaseUrl = getenv("PHP_BASE_URL");
		if(strpos($apiBaseUrl, "localhost") !== false){
			$demFilesRootFolder = "C:\\tmp\demfiles";
			$filesRootFolder = "C:\\tmp\\rcon\maps";
			$apiBaseUrl = "https://marco.selfip.net";
		}
		echo "__URLS = {API_BASE: '".$apiBaseUrl."/zuul'};";
		?>
		$(document).ready(function () {
			Csgo.init();
			PlayersManager.init();
			ChartsManager.init();
			DiscordBot.init();
		});
	</script>

</head>

<body>
	<!-- Top Bar -->
	<nav class="navbar sticky-top navbar-dark bg-dark">
		<span class="navbar-brand mb-0 h1">IXI-GO: Monday Nights</span>
		<div>

		</div>
	</nav>

	<!-- Page Container -->
	<div class="csgo-container">
		<br><br>
		<br><br>
		<!-- Bootstrap Container -->
		<div class="container">
			<ul class="nav nav-tabs" id="myTab" role="tablist">
				<li class="nav-item" role="presentation">
					<a class="nav-link active" id="dem-tab" data-toggle="tab" href="#demDiv" role="tab" aria-controls="dem" aria-selected="true">Dem Files</a>
				</li>
				<li class="nav-item" role="presentation">
					<a class="nav-link" id="players-tab" data-toggle="tab" href="#playersDiv" role="tab" aria-controls="players" aria-selected="false">Players</a>
				</li>
				<li class="nav-item" role="presentation">
					<a class="nav-link" id="charts-tab" data-toggle="tab" href="#chartsDiv" role="tab" aria-controls="charts" aria-selected="false">Charts</a>
				</li>
				<li class="nav-item" role="presentation">
					<a class="nav-link" id="rcon-tab" data-toggle="tab" href="#rconDiv" role="tab" aria-controls="rcon" aria-selected="false">Rcon</a>
				</li>
				<li class="nav-item" role="presentation">
					<a class="nav-link" id="bot-tab" data-toggle="tab" href="#botDiv" role="tab" aria-controls="bot" aria-selected="false">Discord Bot</a>
				</li>
			</ul>
			<div class="tab-content" id="myTabContent">
				<div class="tab-pane fade show active" id="demDiv" role="tabpanel" aria-labelledby="dem-tab">
					<!-- START Dem files Accordion -->
					<div class="accordion" id="accordionExample">
						<?php
						$demFolders = scandir($demFilesRootFolder, 1);
						foreach ($demFolders as $demFolder) {
							if ($demFolder == "." || $demFolder == "..") {
								continue;
							}
							if (is_dir($demFilesRootFolder . "/" . $demFolder)) {
						?>
								<div class="card">
									<div class="card-header" id="card_<?php echo $demFolder; ?>">
										<h2 class="mb-0">
											<button 
												class="btn btn-link btn-card"
												type="button"
												data-toggle="collapse" 
												data-target="#collapse_<?php echo $demFolder; ?>"
												aria-expanded="false"
												aria-controls="collapse_<?php echo $demFolder; ?>">
													<?php
													$date =  date_create_from_format('Y-m-d', $demFolder);
													echo date_format($date, 'd M Y');
													?>
											</button>
										</h2>
									</div>

									<div 
										id="collapse_<?php echo $demFolder; ?>" 
										class="collapse"
										aria-labelledby="card_<?php echo $demFolder; ?>"
										data-parent="#accordionExample">
										<div class="card-body">
											<ul class="list-group">
												<?php
												$demFiles = scandir($demFilesRootFolder . "/" . $demFolder);
												foreach ($demFiles as $demFile) {
													if (!is_dir($demFile)) {
														$fullFileName = $demFilesRootFolder . "/" . $demFolder . "/" . $demFile;
														$fileSize = filesize($fullFileName);
														if ($fileSize < 1048576) {
															continue;
														}
												?>
														<li class="list-group-item">
															<div class="row">
																<div class="col-12 col-sm-8">
																	<?php
																	echo explode("-", $demFile)[4];
																	?>
																</div>
																<div class="col-4 col-sm-3 col-md-3">
																	<?php
																	echo formatSizeUnits($fileSize);
																	?>
																</div>
																<div class="col-1">
																	<a
																		href="<?php echo $fullFileName; ?>"
																		target="_blank">
																		<i 
																			class="fa fa-cloud-download"
																			aria-hidden="true">
																		</i>
																	</a>
																</div>
															</div>
														</li>
												<?php
													}
												}
												?>
											</ul>
										</div>
									</div>
								</div>
						<?php
							}
						}
						?>
					</div>
					<!-- END Dem files Accordion -->
				</div>
				<div class="tab-pane fade" id="playersDiv" role="tabpanel" aria-labelledby="players-tab">
					<!-- START Players -->
					<div class="container-fluid">
						<div class="row">
							<div class="col-12">
								&nbsp;
							</div>
						</div>
						<div class="row">
							<div class="col-12 col-sm-6 col-md-6 col-lg-4 col-xl-4">
								<div class="input-group mb-3">
									<div class="input-group-prepend">
										<label class="input-group-text" for="selectRoundToConsider">Rounds to consider</label>
									</div>
									<select class="custom-select" id="selectRoundToConsider">
									</select>
								</div>
							</div>
							<div class="col-12 col-sm-6 col-md-6 col-lg-4 col-xl-4">
								<div class="input-group mb-3">
									<div class="input-group-prepend">
										<label class="input-group-text" for="selectScoreType">Score Type</label>
									</div>
									<select class="custom-select" id="selectScoreType">
									</select>
								</div>
							</div>
							<div class="col-12 col-sm-6 col-md-6 col-lg-4 col-xl-4">
								<div class="input-group mb-3">
									<div class="input-group-prepend">
										<label class="input-group-text" for="selectMinPercPlayed">Min % Played</label>
									</div>
									<select class="custom-select" id="selectMinPercPlayed">
									</select>
								</div>
							</div>
							<div class="col-12 col-sm-6 col-md-6 col-lg-4 col-xl-4">
								<div class="input-group mb-3">
								<div class="input-group-prepend">
										<label class="input-group-text" for="penaltyWeigth">Penalty Weight</label>
									</div>
									<input id="penaltyWeigth" type="text" class="form-control" placeholder="Penalty Weight" value="0.4">
								</div>
                        	</div>
							<div class="col-12 col-sm-12 col-md-6 col-lg-6 col-xl-4">
								<div class="form-check form-check-inline">
									<input class="form-check-input" type="radio" name="partitionType" id="partitionTypeIxigo" value="IXIGO">
									<label class="form-check-label" for="inlineRadio1">IxiGo</label>
								</div>
								<div class="form-check form-check-inline">
									<input class="form-check-input" type="radio" name="partitionType" id="partitionTypeStandard" value="SIMPLE">
									<label class="form-check-label" for="inlineRadio2">Standard</label>
								</div>
								<div class="form-check form-check-inline">
									<input class="form-check-input" type="radio" name="partitionType" id="partitionTypeEq" value="FORCE_PLAYERS">
									<label class="form-check-label" for="inlineRadio3">Equal Numbers</label>
								</div>
                        	</div>
							<div class="col-12 col-sm-12 col-md-6 col-lg-4 col-xl-4">
								<button id="setPlayerOnServer" type="button" class="btn btn-secondary btn-block">Set these players on the server</button>
                        	</div>
						</div>
						<div class="row">
							<div class="col-12">
								<hr>
							</div>
						</div>
						<div class="row">
							<div class="col-12 col-sm-12 col-md-4">
								<div id="usersListWrapper" class="row"></div>
								<br>
							</div>
							<div class="col-12 col-sm-12 col-md-8">
								<div class="row">
									<div class="col-12 col-md-12 col-lg-6" id="terroristPlayers">
										<div class="card">
											<img src="./pictures/terrorist.jpg" class="card-img-top" alt="Terrorists">
											<div class="card-body">
												<p class="card-text">Team score <span class="badge badge-pill badge-primary"></span></p>
												<ul class="list-group">
												</ul>
											</div>
										</div>
										<br>
									</div>
									<div class="col-12 col-md-12 col-lg-6" id="ctPlayers">
										<div class="card">
											<img src="./pictures/counterterrorist.jpg" class="card-img-top" alt="Terrorists">
											<div class="card-body">
												<p class="card-text">Team score <span class="badge badge-pill badge-primary"></span></p>
												<ul class="list-group">
												</ul>
											</div>
										</div>
										<br>
									</div>
								</div>
							</div>
						</div>
					</div>
					<!-- END Players -->
				</div>
				<div class="tab-pane fade" id="chartsDiv" role="tabpanel" aria-labelledby="charts-tab">
					<!-- START CHARTS -->
					<div class="container-fluid">
						<div class="row">
							<div class="col-12">
								&nbsp;
							</div>
						</div>
						<div class="row">
							<div class="col-12 col-sm-12 col-md-6 col-lg-6 col-xl-6" >
								<div class="input-group mb-3">
									<div class="input-group-prepend">
										<label class="input-group-text" for="chartRoundToConsider">Rounds to consider</label>
									</div>
									<select class="custom-select" id="chartRoundToConsider">
									</select>
								</div>
							</div>
							<div class="col-12 col-sm-12 col-md-6 col-lg-6 col-xl-6" >
								<div class="input-group mb-3">
									<div class="input-group-prepend">
										<label class="input-group-text" for="chartSelectScoreType">Score Type</label>
									</div>
									<select class="custom-select" id="chartSelectScoreType">
									</select>
								</div>
							</div>
						</div>
						<div class="row">
							<div class="col-12">
								<div class="row" id="chartRowPlayers">
								</div>
							</div>
						</div>
						<div class="col-12">
							<canvas id="playerScoresChart"></canvas>
						</div>
						<div class="col-12">
							&nbsp;
						</div>
						<div class="col-12 d-flex justify-content-center">
							<h5>Maps Played</h5>
						</div>
						<div class="col-12">
							<canvas id="mapsPlayedChart"></canvas>
						</div>
					</div>
					<!-- END Charts -->
				</div>
				<div class="tab-pane fade" id="rconDiv" role="tabpanel" aria-labelledby="dem-tab">
					<!-- START RCON -->
					<br>
					<div class="row">
						<div class="col-12 col-sm-12 col-md-12 col-lg-4 col-xl-4">
							<div class="input-group mb-3">
								<div class="input-group-prepend">
									<span class="input-group-text" id="rconHostText">Rcon Host</span>
								</div>
								<input type="text" class="form-control" id="rconHost" value="192.168.1.24">
							</div>
						</div>
						<div class="col-12 col-sm-12 col-md-6 col-lg-4 col-xl-4">
							<div class="input-group mb-3">
								<div class="input-group-prepend">
									<span class="input-group-text" id="rconPortText">Rcon Port</span>
								</div>
								<input type="text" class="form-control" id="rconPort" placeholder="27015" value="27015">
							</div>
						</div>
						<div class="col-12 col-sm-12 col-md-6 col-lg-4 col-xl-4">
							<div class="input-group mb-3">
								<div class="input-group-prepend">
									<span class="input-group-text" id="rconPasswText">Rcon Passw</span>
								</div>
								<input type="password" class="form-control" id="rconPassw">
							</div>
						</div>
						<div class="col-12 col-sm-12 col-md-6 col-lg-4 col-xl-4">
							<div class="input-group mb-3">
								<input type="text" class="form-control" id="rconCmd" placeholder="RCON command to send">
								<div class="input-group-append">
									<button class="btn btn-secondary" type="button" id="sendRcon">Send</button>
								</div>
							</div>
						</div>
					</div>
					<?php 
					/*
					*	Creating the section to issue "Bots" rcon Commands
					*/
					$rconSection = new RconSection("Bots");
					$rconSection->addCard(new PictureRconCard("bot_add_t", "Add a Terrorist Bot", "./pictures/terrorist.jpg"));
					$rconSection->addCard(new PictureRconCard("bot_add_ct", "Add a C.T. Bot", "./pictures/counterterrorist.jpg"));
					$rconSection->addCard(new PictureRconCard("bot_kick", "Kick All the bots", "./pictures/kickbots.jpg"));
					$rconSection->printHtml();

					/*
					*	Creating the section to issue "Game" rcon Commands
					*/
					$rconSection = new RconSection("Game");
					$rconSection->addCard(new IconRconCard("mp_restartgame 5", "Restart Game", "fa fa-refresh"));
					$rconSection->addCard(new IconRconCard("pause", "Pause Game", "fa fa-pause-circle-o"));
					$rconSection->addCard(new IconRconCard("unpause", "Resume Game", "fa fa-play-circle-o"));
					$rconSection->addCard(new IconRconCard("exit", "Stop the Server", "fa fa-stop-circle-o"));
					$rconSection->printHtml();
					
					/*
					*	Creating the section to change the map
					*/
					$rconSection = new RconSection("Maps");

					// Scanning the folder
					//$filesRootFolder = "./rcon/maps";
					$files = scandir($filesRootFolder, 0);
					$maps = array();

					// Creating a key->value map, the key is the simple map name
					foreach ($files as $file) {
						if ($file == "." || $file == "..") {
							continue;
						}
						if (!is_dir($filesRootFolder . "/" . $file)) {
							$arr = explode("-", str_replace(".jpg", "", $file));
							$mapName = $arr[count($arr) - 1];
							$maps[$mapName] = $file;
						}
					}

					ksort($maps);

					// Creating the cards
					foreach ($maps as $map => $file) {
						$rconCmd = "map ".str_replace("-", "/", str_replace(".jpg", "", $file));
						$imgPath = $filesRootFolder . "/" . $file;
						$label = $map;
						$rconSection->addCard(new PictureRconCard($rconCmd, $label, $imgPath));
					}
					$rconSection->printHtml();
					?>
					<!-- END RCON -->
				</div>
				<div class="tab-pane fade" id="botDiv" role="tabpanel" aria-labelledby="bot-tab">
					<!-- START BOT -->
					<div class="container-fluid">
						<div class="row">
							&nbsp;
						</div>
						<div class="row">
							<div class="col-12 col-sm-6 col-md-6 col-lg-4 col-xl-3">
								<div class="input-group mb-3">
									<div class="input-group-prepend">
										<label class="input-group-text" for="botRoundToConsider">Rounds to consider</label>
									</div>
									<select class="custom-select" id="botRoundToConsider">
									</select>
								</div>
							</div>
							<div class="col-12 col-sm-6 col-md-6 col-lg-4 col-xl-3">
								<button id="saveMapDiscordUsers" type="button" class="btn btn-info btn-block">Save Mapping</button>
								<div>&nbsp;</div>
							</div>
							<div class="col-12 col-sm-6 col-md-6 col-lg-4 col-xl-3">
								<button id="startDiscordBot" type="button" class="btn btn-success btn-block">Start The Bot</button>
								<div>&nbsp;</div>
							</div>
							<div class="col-12 col-sm-6 col-md-6 col-lg-4 col-xl-3">
								<button id="stopDiscordBot" type="button" class="btn btn-danger btn-block">Stop The Bot</button>
								<div>&nbsp;</div>
							</div>
						</div>
						<div class="row">
							<div class="col-6">
								<h3>Discord ID</h3>
								<div class="row" id="discordUsers">
									<div class="col-12">
										Please Start the Bot
									</div>
								</div>
							</div>
							<div class="col-6">
								<h3>Steam ID</h3>
								<div class="row" id="mappedSteamUsers">
									<div class="col-12">
										Please Start the Bot
									</div>
								</div>
							</div>
						</div>
					</div>
					<!-- END BOT -->
				</div>
			</div>
		</div>
	</div>
	<!-- Modal -->
	<div class="modal fade" id="modalWindow" tabindex="-1" aria-labelledby="modalWindowLabel" aria-hidden="true">
		<div class="modal-dialog">
			<div class="modal-content">
				<div class="modal-header">
					<h5 class="modal-title" id="modalWindowLabel" style="color: #212529">RCON Response</h5>
					<button type="button" class="close" data-dismiss="modal" aria-label="Close">
						<span aria-hidden="true">&times;</span>
					</button>
				</div>
				<div class="modal-body">
					<div class="input-group">
						<textarea class="form-control" id="rconResp" rows="20"></textarea>
					</div>
				</div>
			</div>
		</div>
	</div>
	<body>

</html>