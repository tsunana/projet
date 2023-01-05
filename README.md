
Premièrement, l'objectif de ce projet est de produire une application-jeu pour géolocaliser et attraper des Miagemone.
Le gameplay est constitué ainsi : 
Le joueur lance l'application avec son appareil (doit être connecté à google service et avoir une caméra). Il renseigne son identifiant et l'application ouvre alors la carte. Si l'identifiant existe dans la base de donnée, le joueur verra le message: "changer de pseudo".
Une fois sur la carte, le joueur reçoit des indications d'itinéraire afin de trouver le Miagémon le plus proche parmi les 5.
Lorsqu'il arrive au niveau du Miagémon, le joueur passe en mode "chasse" et la caméra affiche les alentrours. Quand l'orientation correspond à celle de la position du Miagémon, le joueur peut l'afficher puis le capturer en l'enregistrant dans sa galerie photo.
Une fois capturé, le joueur est notifié du nombre de Miagémons qui lui reste à attaper, son score augmente et l'itinéraire du prochain Miagémon le plus proche est lancé.
Lorsque le joueur a attrapé les 5 Miagémons, la partie est terminée. Le joueur peut alors relancer l'application et obtenir 5 nouveaux Miagémons positionnés au hasard dans le campus de l'Université de Bordeaux.

Miagiste, attrapez-les tous!

Pour réaliser cette application-jeu, nous avons donc utilisé Android Studio et nous avons codé en java (et en xml pour le front)
Nous avons développé plusieurs activités ayant chacune leur utilité:
  - CameraActivity: cette activité permet via une API d'accéder à la caméra de l'utilisateur et d'afficher sur son écran ce que voit la caméra.
  Lorsque celui-ci va bouger dans l'espace la caméra et ce qui s'affiche à l'écran va également suivre son mouvement.
  Lorsque le joueur va prendre en photo le sprite, la photo prise va s'enregistrer dans son appareil.
  - CompassActivity: cette activité sert à afficher la boussole afin de permettre au joueur de trouver le sprite pour le capturer.
  Dans cette activité, nous avons rencontré notre plus grosse difficulté de ce projet :  nous n'avons pas réussi à faire correspondre la boussole avec l'orientation du téléphone en utilisant l'objet Sensor. Nous voulions que l'affichage du sprite soit toujours vers une direction
  (par exemple à l'ouest, alors le joueur devait orienter son téléphone vers l'ouest pour voir le sprite).
  Malheureusement, nous n'avons pas réussi, nous affichons donc seulement un message avec un toast lorsque la caméra est ouverte.
  - DisplaySpriteActivity: Cette activité nous sert à pouvoir afficher et capturer le sprite en format d'image.

  - MainActivity:l'activité main est l'activité centrale de notre projet. On utilise l'API Google Maps.
  En effet, elle sert non seulement à pouvoir géolocaliser le jouer, localiser les sprite autour et lui indiquer comment se
  rendre proche du sprite à capturer. Pour un souci de simplification, nous avons borné la géolocalisation à un périmètre restreint
  autour la localisation du joueur.
  - Nous avons également d'autres fichiers java comme le SpriteSQLiteDatabase qui permet de créer et de gérer les sprite directement dans
  une base de données SQLite.
  
  Pour chaque activité, nous avons son correspondant xml qui permet d'afficher un écran.
  Dans ces fichiers, nous définissons le positionnement, le nom et l'action des boutons.
 Dans les drawable, nous déposons les fichiers (ici principalement en photo png) que nous utilisons dans nos écrans.
 Par exemple, c'est dans ce dossier que l'on dépose les photos des sprite à afficher ensuite.
 
 Dans les fichiers values, nous définissons tout ce qui concerne les éléments graphiques,
 notamment les à afficher quand telle ou telle activité est déclenchée. 
 
 Enfin, le fichier AndroidManifest.xml va non seulement définir les caractéristiques propres au projet,
 mais également les autorisations d'accès à différents élements de l'appareil utilisé.
 
 Concernant les difficultés rencontrées, mis à part notre échec dans l'activité compass, nous avons également rencontré
 des difficultés pour gérer le devenir des Sprite une fois capturés. Notre objectif premier était de supprimer
 l'affichage du sprite une fois capturé. Cependant, nous n'avons pas réussi totalement, alors nous avons trouvé
 une solution différente. Nous avons donc décidé de demander au joueur s'il voulait continuer à jouer une fois le sprite capturé,
 si la réponse est positive alors l'application va sélectionner un sprite parmi ceux existants et le joueur
 pourra partir à sa chasse.
 
 
 
