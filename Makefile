OUTDIR = out
SRCDIR = src
MAIN = coursework3.CW3Main
JFLAGS = -g -cp src
JC = javac
JAVA = java
.SUFFIXES: .java .class

CLASSES = $(shell find src -name *.java)

DSTFILES = $(patsubst src/%.java,out/%.class,$(CLASSES))

$(OUTDIR)/%.class: $(SRCDIR)/%.java
	@mkdir -p $(OUTDIR)
	$(JC) $(JFLAGS) $(SRCDIR)/$*.java -d $(OUTDIR)

default: classes

classes: $(DSTFILES)

clean:
		$(RM) -r out

run: classes
		$(JAVA) -cp out $(MAIN)