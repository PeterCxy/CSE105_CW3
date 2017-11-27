OUTDIR = out
SRCDIR = src
MAIN = coursework3.CW3Main
JFLAGS = -g -cp src
JC = javac
JAVA = java
.SUFFIXES: .java .class

CLASSES = \
		src/cw3interfaces/CommunityGroupInterface.java \
		src/cw3interfaces/SkillSorterInterface.java \
		src/cw3interfaces/VolunteerInterface.java \
		src/coursework3/CommunityGroup.java \
		src/coursework3/Constants.java \
		src/coursework3/CW3Main.java \
		src/coursework3/SkillSorter.java \
		src/coursework3/Volunteer.java

DSTFILES = $(patsubst src/%.java,out/%.class,$(CLASSES))

$(OUTDIR)/%.class: $(SRCDIR)/%.java
	$(JC) $(JFLAGS) $(SRCDIR)/$*.java -d $(OUTDIR)

default: classes

classes: $(DSTFILES)

clean:
		$(RM) -r out

run: classes
		$(JAVA) -cp out $(MAIN)