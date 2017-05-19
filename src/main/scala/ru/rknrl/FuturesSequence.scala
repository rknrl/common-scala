package ru.rknrl

import scala.concurrent.{ExecutionContext, Future}

object FuturesSequence {
  def seq[T](futures: Seq[() ⇒ Future[T]])(implicit executor: ExecutionContext) =
    if (futures.isEmpty)
      Future.successful(IndexedSeq.empty)
    else
      new FuturesSequence(futures).run
}

private class FuturesSequence[T](futures: Seq[() ⇒ Future[T]]) {
  var index = 0
  var results = IndexedSeq.empty[T]

  def run()(implicit executor: ExecutionContext): Future[IndexedSeq[T]] =
    futures(index)() flatMap runNext

  def runNext(result: T)(implicit executor: ExecutionContext): Future[IndexedSeq[T]] = {
    index = index + 1
    results = results :+ result

    if (index < futures.size)
      run()
    else
      Future.successful(results)
  }
}