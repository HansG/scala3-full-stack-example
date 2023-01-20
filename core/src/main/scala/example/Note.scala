package example

import upickle.default.*

final case class Note(id: String, title: String, content: String):
  def dup = Note2(this, this.copy(title = "CC: "+title))

object Note:
  given ReadWriter[Note] = macroRW


final case class Note2(note1: Note, note2: Note)

object Note2:
  given ReadWriter[Note2] = macroRW
