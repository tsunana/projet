**Miagiste, attrapez-les tous!**

Premièrement, l'objectif de ce projet est de produire une application-jeu pour géolocaliser et attraper des Miagemone. 
Il est nécessaire d'avoir un téléphone Android doté d'une connection internet pour la connection avec l'API Google ainsi qu'une caméra.

GamePlay : 
Une fois l'application lancé sur le téléphone, le joueur à la possibilité de capturer des Miagémones, un par un. Un premier Miagémone apparait sur la carte, une fois attrapé, un nouveau Miagémone est affiché sur la carte. Une fois que les 5 Miagémones créés on été attrapés, le jeu affiche un page de fin.

Fonctionnement : 
L'application s'ouvre sur une demi page. Le bas de la page est la carte indiquant la position du joueur en rouge et celle du Miagémone en bleu. La carte affiche une ligne rouge reliant la position du joueur au Miagémone, qui sert d'itinéraire pour rejoindre ce dernier. Le haut de la page donne des indications sur la proximité avec le Miagémone. A moins de 50m un message apparait sur la partie haute de l'écran, à moins de 5m le bouton permettant d'ouvrir la caméra apparait. Lorsqu'il arrive à 5m du Miagémone, le joueur peut passe en mode "chasse" et ouvrir la caméra, dans laquelle est superposée une boussole. Un message apparait pour indiquer au joueur de se positioner à l'Ouest avant de capturer le Miagémone. Le joueur peut ensuite appuyer sur la Miagéball pour capturer le Miagémone, qui est ensuite affiché une fois attrapé. Il est enregistré dans les fichiers du téléphone. Les Miagémones sont choisis au hasard parmi une liste de 5 et sont affichés l'un aprés l'autre. Une fois qu'un a été attrapé, il est supprimé de la liste pour ne pas réapparaitre. Lorsque le joueur a attrapé les 5 Miagémones générés au hasard, la partie est terminée. 



Pour réaliser cette application-jeu, nous avons donc utilisé Android Studio et nous avons codé en java (et en xml pour le front). Nous avons développé plusieurs activités ayant chacune leur utilité:

MainActivity:l'activité main est l'activité centrale de notre projet. On utilise l'API Google Maps. En effet, elle sert non seulement à pouvoir géolocaliser le jouer, localiser les sprites autour et lui indiquer comment se rendre proche du sprite à capturer. Pour un souci de simplification, nous avons borné la géolocalisation à un périmètre restreint autour la localisation du joueur. 

CameraActivity: cette activité permet via une API d'accéder à la caméra de l'utilisateur et d'afficher sur son écran ce que voit la caméra. Lorsque celui-ci va bouger dans l'espace la caméra et ce qui s'affiche à l'écran va également suivre son mouvement. Lorsque le joueur va prendre en photo le sprite, la photo prise va s'enregistrer dans son appareil.

CompassActivity: cette activité sert à afficher la boussole afin de permettre au joueur de trouver le sprite pour le capturer. Dans cette activité, nous avons rencontré notre plus grosse difficulté de ce projet : nous n'avons pas réussi à faire correspondre la boussole avec l'orientation du téléphone en utilisant l'objet Sensor. Nous voulions que l'affichage du sprite soit toujours vers une direction (par exemple à l'ouest, alors le joueur devait orienter son téléphone vers l'ouest pour voir le sprite). Malheureusement, nous n'avons pas réussi, nous affichons donc seulement un message avec un toast lorsque la caméra est ouverte.

DisplaySpriteActivity: Cette activité nous sert à pouvoir afficher et capturer le sprite en format d'image. L'image correspond au nom du sprite séléctionné au hasard. 

SpriteDAO : Permet de créer et de gerer les sprites dans la base de donnée SpriteSQLiteDatabase


Pour chaque activité, nous avons son correspondant xml qui permet d'afficher un écran. Dans ces fichiers, nous définissons le positionnement, le nom et l'action des boutons. Dans les drawable, nous déposons les fichiers (ici principalement en photo png) que nous utilisons dans nos écrans. Par exemple, c'est dans ce dossier que l'on dépose les photos des sprite à afficher ensuite.

Dans les fichiers values, nous définissons tout ce qui concerne les éléments graphiques, notamment les à afficher quand telle ou telle activité est déclenchée. 

Enfin, le fichier AndroidManifest.xml va non seulement définir les caractéristiques propres au projet, mais également les autorisations d'accès à différents élements de l'appareil utilisé.

 
