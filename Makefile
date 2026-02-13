JAVAC = javac
SOURCE_DIR = src/main/java/com/lmu/SlitherThink/Grille
OUTPUT_DIR = temp_class
SOURCES = $(SOURCE_DIR)/Test.java $(SOURCE_DIR)/Case.java $(SOURCE_DIR)/Trait.java $(SOURCE_DIR)/Matrice.java $(SOURCE_DIR)/ValeurTrait.java

.PHONY: all clean run

all: $(OUTPUT_DIR)/Test.class

$(OUTPUT_DIR)/Test.class: $(SOURCES)
	@mkdir -p $(OUTPUT_DIR)
	$(JAVAC) -d $(OUTPUT_DIR) $(SOURCES)

run: $(OUTPUT_DIR)/Test.class
	java -cp $(OUTPUT_DIR) Test

clean:
	rm -rf $(OUTPUT_DIR)/*.class