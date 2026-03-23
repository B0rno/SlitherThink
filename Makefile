JAVAC = javac
SOURCE_BASE = src/main/java/com/lmu/SlitherThink
SOURCE_DIR = $(SOURCE_BASE)/Grille
SAVE_DIR = $(SOURCE_BASE)/save
OUTPUT_DIR = temp_class

SOURCES = \
	$(SOURCE_DIR)/Test.java \
	$(SOURCE_DIR)/Case.java \
	$(SOURCE_DIR)/Trait.java \
	$(SOURCE_DIR)/ValeurTrait.java \
	$(SOURCE_DIR)/Matrice.java \
	$(SAVE_DIR)/*.java

OUTPUT_PACKAGE = $(OUTPUT_DIR)/com/lmu/SlitherThink

.PHONY: all clean run

all: $(OUTPUT_DIR)/com/lmu/SlitherThink/Grille/Test.class

$(OUTPUT_DIR)/com/lmu/SlitherThink/Grille/Test.class: $(SOURCES)
	@mkdir -p $(OUTPUT_DIR)
	$(JAVAC) -d $(OUTPUT_DIR) $(SOURCES)

run: $(OUTPUT_DIR)/com/lmu/SlitherThink/Grille/Test.class
	java -cp $(OUTPUT_DIR) com.lmu.SlitherThink.Grille.Test

clean:
	rm -rf $(OUTPUT_DIR)

.PHONY: help
help:
	@echo "Targets disponibles:"
	@echo "  make all   - Compile tous les fichiers"
	@echo "  make run   - Exécute le programme"
	@echo "  make clean - Supprime les fichiers compilés"