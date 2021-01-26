<?php
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
	<meta name="viewport" content="width=device-width, initial-scale=1">
	<meta name="viewport" id="vp" content="initial-scale=1.0,user-scalable=no,maximum-scale=1" />
	<meta name="apple-mobile-web-app-status-bar-style" content="black">
	<meta name="description" content="IXI-GO : Every Monday evening we play Counter Strike Global Offencive (CSGO) on our dedicated server. Here is we share the game results and some info" />
	<meta property="og:title" content="IXI-GO: Monday Nights" />
	<meta property="og:description" content="IXI-GO : Every Monday evening we play Counter Strike Global Offencive (CSGO) on our dedicated server. Here is we share the game results and some info" />
	<meta property="og:type" content="website" />
	<meta property="og:url" content="http://marco.selfip.net/cstrike" />
	<meta property="og:image" content="csgo.jpg" />
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
	<script src="./js/pnotify.custom.min.js"></script>
	<script src="./js/MarcoUtils.js"></script>
	<script src="./js/Csgo.js"></script>

	<script>
		$(document).ready(Csgo.init);
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
					<a class="nav-link" id="todo-tab" data-toggle="tab" href="#todoDiv" role="tab" aria-controls="todo" aria-selected="false">Rounds</a>
				</li>
				<li class="nav-item" role="presentation">
					<a class="nav-link" id="rcon-tab" data-toggle="tab" href="#rconDiv" role="tab" aria-controls="todo" aria-selected="false">Rcon</a>
				</li>
			</ul>
			<div class="tab-content" id="myTabContent">
				<div class="tab-pane fade show active" id="demDiv" role="tabpanel" aria-labelledby="dem-tab">
					<!-- START Dem files Accordion -->
					<div class="accordion" id="accordionExample">
						<?php
						$demFilesRootFolder = "./demfiles";
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
				<div class="tab-pane fade" id="todoDiv" role="tabpanel" aria-labelledby="todo-tab">
					<!-- START round files Accordion -->
					<div class="accordion" id="accordionExample">
						<?php
						$roundFilesRootFolder = "./rounds";
						$demFolders = scandir($roundFilesRootFolder, 1);
						foreach ($demFolders as $demFolder) {
							if ($demFolder == "." || $demFolder == "..") {
								continue;
							}
							if (is_dir($roundFilesRootFolder . "/" . $demFolder)) {
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
												$demFiles = scandir($roundFilesRootFolder . "/" . $demFolder);
												foreach ($demFiles as $demFile) {
													if (!is_dir($demFile)) {
														$fullFileName = $roundFilesRootFolder . "/" . $demFolder . "/" . $demFile;
														$fileSize = filesize($fullFileName);
														if ($fileSize < 1) {
															continue;
														}
												?>
														<li class="list-group-item">
															<div class="row">
																<div class="col-12 col-sm-8">
																	<?php
																	echo explode("_", $demFile)[1];
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
					<!-- END round files Accordion -->
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
								<input type="text" class="form-control" id="rconHost">
							</div>
						</div>
						<div class="col-12 col-sm-12 col-md-6 col-lg-4 col-xl-4">
							<div class="input-group mb-3">
								<div class="input-group-prepend">
									<span class="input-group-text" id="rconPortText">Rcon Port</span>
								</div>
								<input type="text" class="form-control" id="rconPort">
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
					</div>
					<div class="rcon-container">
						<?php
							$filesRootFolder = "./rcon/maps";
							$files = scandir($filesRootFolder, 1);
							foreach ($files as $file) {
								if ($file == "." || $file == "..") {
									continue;
								}
								if (!is_dir($filesRootFolder . "/" . $file)) {
						?>
						<div class="card rcon-map" data-rcon-cmd="<?php echo "map ".str_replace(".jpg", "", $file);?>">
							<img class="card-img-top" src="<?php echo $filesRootFolder . "/" . $file;?>" alt="Card image cap">
							<div class="card-body">
								<p class="card-text"><?php echo str_replace(".jpg", "", $file);?></p>
							</div>
						</div>
						<?php 
								}
							}
						?>
					</div>
					<!-- END RCON -->
				</div>
			</div>
		</div>
	</div>

	<body>

</html>