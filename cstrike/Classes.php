<?php
interface RconCard {
    public function getHtml();
}

class RconSection {
    var $title;
    var $cards = array();


    function __construct($title){
        $this->title = $title;
    }

    public function addCard(RconCard $rconCard){
        array_push($this->cards, $rconCard);
    }

    public function printHtml(){
        $str = <<<EOD
        <div class="rcon-container-title">
            <h1>$this->title</h1>
        </div>
        <div class="rcon-container">
EOD;
        echo $str;
        foreach ($this->cards as $card) {
            echo $card->getHtml();
        }
        echo "</div>";
    }
}

class PictureRconCard implements RconCard {
    var $rconCmd;
    var $cardDesc;
    var $imgPath;

    function __construct($rconCmd, $cardDesc, $imgPath){
        $this->rconCmd = $rconCmd;
        $this->cardDesc = $cardDesc;
        $this->imgPath = $imgPath;
    }
  
    public function getHtml(){
        $html = <<<EOD
        <div class="card rcon-map" data-rcon-cmd="$this->rconCmd">
            <img class="card-img-top" src="$this->imgPath" alt="Card image cap">
            <div class="rcon-card-body">
            $this->cardDesc
            </div>
        </div>
EOD;
        return $html;
    }
}

class IconRconCard implements RconCard
{
    var $rconCmd;
    var $cardDesc;
    var $fontAwesomeIconClass;

    function __construct($rconCmd, $cardDesc, $fontAwesomeIconClass){
        $this->rconCmd = $rconCmd;
        $this->cardDesc = $cardDesc;
        $this->fontAwesomeIconClass = $fontAwesomeIconClass;
    }
  
    public function getHtml(){
        $html = <<<EOD
        <div class="card rcon-map" data-rcon-cmd="$this->rconCmd">
            <div class="rcon-icon-div">
                <i class="$this->fontAwesomeIconClass" aria-hidden="true"></i>
            </div>
            <div class="rcon-card-body">
                $this->cardDesc
            </div>
        </div>
EOD;
        return $html;
    }
}

?>
