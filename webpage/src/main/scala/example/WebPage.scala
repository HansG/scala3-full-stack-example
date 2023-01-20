package example

import org.scalajs.dom.html.Element
import org.scalajs.dom.document
import org.scalajs.dom.html.*

import DomHelper.*

import scala.concurrent.ExecutionContext
import scala.util.control.NonFatal

object WebPage:
  given ExecutionContext = ExecutionContext.global
  val service = new HttpClient()

  val titleInput = input()
  val contentTextArea = textarea()

  val saveButton = button("Create Note")
  saveButton.onclick = _ =>
    service
      .createNote(titleInput.value, contentTextArea.value)
      .map(addNote)

  val form: Div = div(
    titleInput,
    contentTextArea,
    saveButton
  )
  form.className = "note-form"

  val appContainer: Div = div(
    h1("My Notepad"),
    form
  )
  appContainer.id = "app-container"

  def addNote(note: Note): Unit =
    val elem = div(
      h2(note.title),
      p(note.content)
    )
    
  def addNote(note: Note2): Unit =
    val elem = div(
      h2(note.note1.title+ " - "+note.note2.title),
      p(note.note1.content),
      p(note.note2.content)
    )

    val deleteButton = button("Delete Note")
    deleteButton.onclick = _ =>
      service
        .deleteNote(note.note1.id)
        .map(res => if res then appContainer.removeChild(elem))

    elem.appendChild(deleteButton)
    elem.className = "note"
    appContainer.appendChild(elem)

  @main def start: Unit =
    document.body.appendChild(appContainer)

    for notes <- service.getAllNotes(); note <- notes do addNote(note)
