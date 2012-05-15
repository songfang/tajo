package nta.engine.planner.physical;

import nta.catalog.Schema;
import nta.datum.Datum;
import nta.engine.SubqueryContext;
import nta.engine.planner.logical.GroupbyNode;
import nta.storage.Tuple;
import nta.storage.VTuple;

import java.io.IOException;

/**
 * This is the sort-based Aggregation Operator.
 * 
 * @author Hyunsik Choi
 */
public class SortAggregateExec extends AggregationExec {
  private Tuple prevKey = null;
  private Tuple newAggTuple = null;
  private Tuple aggTuple = null;

  public SortAggregateExec(SubqueryContext ctx, GroupbyNode annotation,
                           PhysicalExec subOp) throws IOException {
    super(ctx, annotation, subOp);
    this.newAggTuple = new VTuple(outputSchema.getColumnNum());
  }

  @Override
  public Tuple next() throws IOException {
    Tuple curKey;
    Tuple tuple;
    Tuple finalTuple;
    while(!ctx.isStopped() && (tuple = subOp.next()) != null) {
      // build a key tuple
      curKey = new VTuple(keylist.length);
      for(int i = 0; i < keylist.length; i++) {
        curKey.put(i, tuple.get(keylist[i]));
      }

      if (prevKey == null || prevKey.equals(curKey)) {
        if (prevKey == null) {
          for(int i = 0; i < outputSchema.getColumnNum(); i++) {
            Datum datum =
                evals[i].eval(inputSchema, tuple);
            this.newAggTuple .put(i, datum);
          }
          prevKey = curKey;
          this.aggTuple = this.newAggTuple;
        } else {
          // aggregate
          for (int idx : measurelist) {
            Datum datum = evals[idx].eval(inputSchema, tuple,
                aggTuple.get(idx));
            aggTuple.put(idx, datum);
          }
        }
      } else {
        // finalize aggregate and return
        finalTuple = aggTuple;
        this.aggTuple = new VTuple(outputSchema.getColumnNum());
        for(int i = 0; i < outputSchema.getColumnNum(); i++) {
          Datum datum =
          evals[i].eval(inputSchema, tuple);
          this.aggTuple .put(i, datum);
        }
        prevKey = curKey;
        return finalTuple;
      }
    }
    finalTuple = aggTuple;
    aggTuple = null;
    return finalTuple;
  }

  @Override
  public Schema getSchema() {
    return outputSchema;
  }

  @Override
  public void rescan() throws IOException {
    prevKey = null;
    subOp.rescan();
  }
}
