########

Exercice Entretien Stage Flitter 
François Goujon

########

On cherche à déterminer la consommation d'eau à partir d'une photo du compteur. On veut déterminer l'index de consommation (chiffre fond noir, pas les rouges)

J'ai commencé par découvrir les images et utilisé quelques fonctions de traitement d'image classiques, filtres principalement, pour essayer de faire ressortir le texte. J'ai également essayé de gérer les images mal orientées mais cela ne marche pas toujours et est compliqué à perfectionner, je n'ai donc pas passé de temps la dessus et remis les images correctement à la main. Ayant déjà effectué de l'OCR avec Tesseract précédemment, même si celui si marche principalement avec des documents structurés et imprimés, j'ai quand même voulu essayer mais sans résultat. J'ai également essayé des méthode d'OpenCV pour l'extraction de bounding box mais sans résultat non plus.

J'ai donc opté pour la stratégie suivante pour essayer de déterminer le label des images :
	
	- Passez les images dans un premier modèle qui va permettre d'extraire les régions d'intérêt (ROI) de l'image ; ici partie de l'image où on a une grande probabilité d'avoir la zone avec l'index. Pour cela il faut utiliser des modèles d'object détection pour trouver les zones de texte. Ces modèles nécessitent d'être entraînés avec des label qui donnent la position de la boite et son texte (x1 y1 x2 y2 str avec x1,y1 coin haut gauche et x2,y2 coin bas droit et str le texte). Étant donné que ces informations ne sont pas présentes dans les labels de train, je l'ai fait à la main pour 32 images du dossier afin d'utiliser des poids pré-entraînés puis de ré-entrainer sur ces 32 images pour améliorer le résultat. J'ai choisi d'utiliser un modèle YOLO (You Only Look Once) basé sur une implémentation de MobileNetV2 de keras que j'avais déjà utilisé dans un précédent projet.
Le modèle va donner une ou plusieurs ROI pour chaque image, s'il y en a plusieurs on prend l'intersection des ces boites. Cela permet de réduire chaque image à une zone plus petite où est censé se trouver l'index. On essayera ensuite de lire ce qu'il y a dans cette image. (Partie implémentée dans BoundingBox_yolo.ipynb)

	- On passe les données dans un second modèle qui est un pipeline d'un modèle de détection de text et de reconnaissance de charactères/mots. Ce pipeline est issu du package keras-ocr (https://github.com/faustomorales/keras-ocr) qui est un modele de détection de texte CRAFT suivi d'un modele CRNN, c'est un bon package pour effectuer de la détection et reconnaissance de texte non structuré. On récupère ensuite les mots dans l'image, on ne conserve que ceux qui sont des chiffres et dont la boîte est d'une taille suffisamment grande dans l'image (le premier modèle est censé avoir laissé une ROI avec presque seulement l'index dans l'image). On a alors en principe une prédiction. En réalité, à cause des écritures sur le compteur d'eau, il y a souvent plusieurs lectures. Pour améliorer les résultats, il faudrait également rentraîner les 2 modèles de ce pipeline avec nos données, comme expliqué ici : https://github.com/faustomorales/keras-ocr/blob/master/docs/examples/end_to_end_training.rst , je n'ai pas fait car cela aurait pris trop de temps. (Partie implémentée dans LectureROI.ipynb)


Les temps de d'entraînement sont un peu long, je n'ai pas poussé les résultats ou trop fait de fine-tuning, je me suis concentré sur le processus complet pour arriver à un label. Sur certaines images (principalement celles prise assez proprement), les résultats sont assez bon. Avec un entraînement complet et du finetuning je pense qu'il serait possible d'arriver à de bons résultats.


Structure dossier : 

Datayolo : Dossier pour le modele YOLO
-dossier images : 32 images avec bounding box labélisées 
-dossier ground_truth : les 32 fichiers txt label
X.npy, Y.npy, train_id.npy : array numpy générés dans Preprocess.ipynb pour les 32 images : images, labels, id des 32 images choisies

Data : Dossier avec les images ou format numpy
labels.npy, X_train.npy, X_train_ced.npy, X_train_thd.npy : array numpy générés dans Preprocess.ipynb pour toutes les images de pictures : labels, images originales, images avec canny edge, images avec thresholding OTSU.

listeintertrain.npy, listeinterval.npy : array numpy générés à la fin de BoundingBox_yolo.ipynb, contiennt les ROI de l'ensemble train et val (28 et 4 images)

model:
model json et poids pré-entraînés

model1:
poids pré-entraînés ré-entraînés sur les 32 images.

Je n'ai laissé que les fichiers ground_truth et le model json pour alleger le dossier.

Dans l'ordre :

	- checkData.ipynb : lecture de quelques images et essais de filtres et de lecture avec tesseract
	- Preprocess.ipynb : création des fichiers .npy utiles pour la suite
	- BoundingBox_yolo.ipynb : création et entraînement du modèle. Gestion des résultats du modèle pour donner une ROI. Quelques essais avec tesseract sur les ROI.
	- LectureROI.ipynb : utilisation de l'API pipeline de keras-ocr pour la lecture des ROI et résultats.
