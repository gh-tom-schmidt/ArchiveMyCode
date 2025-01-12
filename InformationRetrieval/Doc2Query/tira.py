from tira.third_party_integrations import ensure_pyterrier_is_loaded
from tira.rest_api_client import Client

ensure_pyterrier_is_loaded()
tira = Client()

from pyterrier import get_dataset, IterDictIndexer, BatchRetrieve, IndexFactory
import pyterrier_doc2query

# dataset = "irds:ir-lab-wise-2024/subsampled-ms-marco-deep-learning-20241201-training"
# attempt_name = "bm25-doc2query-subsampled-ms-marco-deep-learning-20241201-training-inforet2024_25"

# dataset = "irds:ir-lab-wise-2024/subsampled-ms-marco-rag-20250105-training"
# attempt_name = "bm25-doc2query-subsampled-ms-marco-rag-20250105-training-inforet2024_25"

dataset = "irds:ir-lab-wise-2024/subsampled-ms-marco-ir-lab-20250105-test"
attempt_name = "bm25-doc2query-subsampled-ms-marco-ir-lab-20250105-test-inforet2024_25"

pt_dataset = get_dataset(dataset)

# append generated queries to the orignal document text
doc2query = pyterrier_doc2query.Doc2Query(append=True, verbose=True)

meta = {
    'docno': 50,  # set this to at least 43 or higher to avoid truncation
    'text': 1000000  # adjusted value for text length
}

indexer = doc2query >> IterDictIndexer("./data/index", meta=meta)

index = indexer.index(pt_dataset.get_corpus_iter())

try:
    # attempt to open the index
    loaded_index = IndexFactory.of("/content/data/index")
    # perform a basic operation
    print(f"Index contains {loaded_index.getCollectionStatistics().getNumberOfDocuments()} documents.")
except Exception as e:
    print(f"Failed to validate index: {e}")

bm25 = BatchRetrieve(index, wmodel="BM25")

run = bm25(pt_dataset.get_topics('text'))

import pickle

# save the 'run' object to a file
with open(f"/content/data/{attempt_name}.pkl", 'wb') as f:
    pickle.dump(run, f)

